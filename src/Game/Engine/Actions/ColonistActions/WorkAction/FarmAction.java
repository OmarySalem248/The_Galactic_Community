package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Buildings.BuildingType;
import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Inventory.Items.Seed.Seed;

import java.util.ArrayList;
import java.util.List;

public class FarmAction extends WorkAction {

    private static final float NEAR_FULL_RATIO = 0.85f;

    public FarmAction(ActionManager colonist) {
        super("Farming", colonist);
    }

    @Override
    public boolean execute() {
        if (!(colonistam.getCurrentTile().getBuilding() instanceof Farm farm)) {
            return true;
        }

        // No seeds anywhere and no active incubators — go search for seeds

        boolean noSeeds = !colonistam.getColonist().getInventory().hasType(ItemType.SEED)
                && !farm.getInv().hasType(ItemType.SEED)
                && myIncubators(farm).isEmpty();
        if (noSeeds && !colonistam.getSearching()) {
            colonist.setStatus("Out of seeds, searching for storage...");
            colonistam.searchFor(ItemType.SEED, BuildingType.STORAGE);
            return true;
        }


        // Farm inventory near full — transport jumps to top priority
        if (isFarmNearFull(farm) && hasGoodsToTransport(farm)) {
            System.out.println("1");
            transportGoods(farm);
            return false;
        }

        // Normal priority gate — one action per tick
        if (hasSeedsToDeposit() && !canPlant(farm)) {
            depositSeeds(farm);
            return false;
        }
        if (hasMyIncubatorsToTend(farm)) {
            tendIncubators(farm);
            return false;
        }
        if (hasMyIncubatorsToHarvest(farm)) {
            harvest(farm);
            return false;
        }
        if (canPlant(farm)) {
            plant(farm);
            return false;
        }

        // Last resort — transport excess goods
        if (hasGoodsToTransport(farm)) {
            System.out.println(2);
            transportGoods(farm);
            return false;
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // Ownership helpers
    // -------------------------------------------------------------------------

    /** Returns only the incubators assigned to this farmer. */
    private List<PlantIncubater> myIncubators(Farm farm) {
        List<PlantIncubater> mine = new ArrayList<>();
        for (PlantIncubater inc : farm.getActiveInc()) {
            if (inc.isAssignedTo(colonist)) mine.add(inc);
        }
        return mine;
    }



    // -------------------------------------------------------------------------
    // Deposit
    // -------------------------------------------------------------------------
    private boolean hasSeedsToDeposit() {
        return colonistam.getColonist().getInventory().hasType(ItemType.SEED);
    }

    private void depositSeeds(Farm farm) {
        List<ItemStack> seeds = new ArrayList<>(
                colonistam.getColonist().getInventory().getByType(ItemType.SEED)
        );
        for (ItemStack seed : seeds) {
            int qty = seed.getQuantity();
            int added = farm.getInv().add(seed.getItem(), qty);
            if (added > 0) {
                colonistam.getColonist().getInventory().remove(seed.getItem(), added);
            }
        }
    }
    // -------------------------------------------------------------------------
    // Tend — only this farmer's incubators
    // -------------------------------------------------------------------------
    private boolean hasMyIncubatorsToTend(Farm farm) {
        return myIncubators(farm).stream().anyMatch(PlantIncubater::needsTending);
    }

    private void tendIncubators(Farm farm) {

        for (PlantIncubater inc : myIncubators(farm)) {
            if (inc.needsTending()) {
                inc.tendto();
                colonist.addtendcount();
                colonist.setStatus("Just tended to " + inc.getPlant().getName());
                colonist.modEnergy(-1);
                break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Harvest — only this farmer's incubators
    // -------------------------------------------------------------------------
    private boolean hasMyIncubatorsToHarvest(Farm farm) {
        return myIncubators(farm).stream().anyMatch(PlantIncubater::canHarvest);
    }

    private void harvest(Farm farm) {

        PlantIncubater inc = myIncubators(farm).stream()
                .filter(PlantIncubater::canHarvest)
                .findFirst()
                .orElse(null);
        if (inc == null) return;

        for (ItemStack stack : inc.getPlant().getHarvestYield()) {
            farm.getInv().add(stack.getItem(), stack.getQuantity());

        }


        farm.clearInc(inc);


    }

    // -------------------------------------------------------------------------
    // Plant — new incubators are automatically assigned to this farmer
    // -------------------------------------------------------------------------
    private boolean canPlant(Farm farm) {
        return (farm.getInv().hasType(ItemType.SEED)|| colonist.getInventory().hasType(ItemType.SEED))
                && farm.hasAvailableIncubator();
    }
    private Seed getSeed(Farm farm){
        Inventory target;
        if(farm.getInv().hasType(ItemType.SEED)){
            target = farm.getInv();

        }
        else {
            target = colonist.getInventory();
        }
        Seed seed = (Seed) target.getByType(ItemType.SEED).get(0).getItem();
        target.remove(seed, 1);
        return seed;
    }
    private void plant(Farm farm) {
        Item seed = getSeed(farm);
        farm.plant((Seed) seed, colonistam.getEventBus(), colonist);
        colonist.setStatus("Planted a " + seed.getName());
    }

    // -------------------------------------------------------------------------
    // Transport
    // -------------------------------------------------------------------------
    private boolean isFarmNearFull(Farm farm) {
        float weight = farm.getInv().getCurrentWeight();
        float max    = farm.getInv().getMaxWeight();
        return max > 0 && (weight / max) >= NEAR_FULL_RATIO;
    }

    private boolean hasGoodsToTransport(Farm farm) {
        return farm.getInv().hasAvailableType(ItemType.FOOD);
    }

    private void transportGoods(Farm farm) {
        if (colonistam.getColonist().getInventory().hasType(ItemType.FOOD)) {
            deliverToStorage(farm);
            return;
        }

        if (!farm.getInv().claimTransport(ItemType.FOOD)) return;

        List<ItemStack> goods = new ArrayList<>(farm.getInv().getByType(ItemType.FOOD));
        for (ItemStack stack : goods) {
            int added = colonistam.getColonist().getInventory().add(stack.getItem(), stack.getQuantity());
            if (added > 0) farm.getInv().remove(stack.getItem(), added);
        }

        if (colonistam.getColonist().getInventory().hasType(ItemType.FOOD)) {
            colonistam.setPendingSourceInventory(farm.getInv());
            colonist.setStatus("Transporting goods to storage...");
            deliverToStorage(farm);
        } else {
            farm.getInv().releaseTransportClaim(ItemType.FOOD);
        }
    }

    private void deliverToStorage(Farm farm) {
        System.out.println("leo");
        var preferred = farm.getPreferredStorage();
        if (preferred != null) {
            colonistam.setDeliveryTarget(preferred);
        } else if (!colonistam.getSearching()) {

            colonistam.searchForBuilding(BuildingType.STORAGE);
        }
    }

    // -------------------------------------------------------------------------
    // Early leave
    // -------------------------------------------------------------------------
    @Override
    public boolean hasNothingLeftToDo() {
        if (!(colonistam.getCurrentTile().getBuilding() instanceof Farm farm)) return false;

        boolean hasSeedsSomewhere = colonistam.getColonist().getInventory().hasType(ItemType.SEED)
                || farm.getInv().hasType(ItemType.SEED);
        boolean myIncNeedTend = myIncubators(farm).stream().anyMatch(PlantIncubater::needsTending);
        boolean myIncMature   = myIncubators(farm).stream().anyMatch(PlantIncubater::canHarvest);
        boolean hasGoodsToMove = farm.getInv().hasAvailableType(ItemType.FOOD)
                || colonistam.getColonist().getInventory().hasType(ItemType.FOOD);

        if (hasSeedsSomewhere || myIncNeedTend || myIncMature
                || hasGoodsToMove || colonistam.getSearching()) return false;

        return colonistam.getMemory().knowsOf(BuildingType.STORAGE);
    }
}