package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.BuildingType;
import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Memory.ToDo;
import Game.Engine.Colonist.Memory.TodoType;
import Game.Engine.Inventory.Delivery;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Inventory.Items.Seed.Seed;
import Game.Engine.Time.GameTime;

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
            return false;
        }


        // Farm inventory near full — transport jumps to top priority
        if (isFarmNearFull(farm)) {
            System.out.println("full");

            transportGoods(farm);
            return false;
        }

        // Normal priority gate — one action per tick
        if (hasSeedsToDeposit() && !canPlant(farm)) {
            System.out.println("deposit");
            depositSeeds(farm);
            return false;
        }
        if (hasMyIncubatorsToTend(farm)) {
            System.out.println("tend");
            tendIncubators(farm);
            return false;
        }
        if (hasMyIncubatorsToHarvest(farm)) {
            System.out.println("harvest");
            harvest(farm);
            return false;
        }
        if (canPlant(farm)) {
            System.out.println("plant");
            plant(farm);
            return false;
        }

        // Last resort — transport excess goods
        if (hasGoodsToTransport(farm)) {

            transportGoods(farm);
            return false;
        }

        return false;
    }
    @Override
    public void setReminder(Building building) {
        Farm farm = (Farm) building;
        System.out.println("remember");
        GameTime present = colonistam.getTime();
        System.out.println("present");
        System.out.println(present);
        GameTime todoTime = null;
        List<PlantIncubater> myIncs = myIncubators(farm);
        if(!myIncs.isEmpty()){
            for(PlantIncubater inc: myIncs){
                System.out.println("new");
                GameTime newtime = colonistam.getMemory().setTime(present,inc.getTimeTill());
                System.out.println(newtime);
                if(todoTime == null){
                    todoTime = newtime;
                }
                else if(!todoTime.lessThan(newtime)){
                    todoTime = newtime;
                }
            }
            System.out.println("Final Todo time:");
            System.out.println(todoTime);
            colonistam.getMemory().addToDo(new ToDo(farm.getCoords().get(0),todoTime, TodoType.WORK ));

        }
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

        List<ItemStack> yield = inc.getPlant().getHarvestYield();

        // Check if everything fits before touching the inventory
        float totalWeight = 0;
        for (ItemStack stack : yield) {
            totalWeight += stack.getItem().getWeight() * stack.getQuantity();
        }

        float available = farm.getInv().getMaxWeight() - farm.getInv().getCurrentWeight();
        if (totalWeight > available) {
            colonist.setStatus("Farm full, waiting to harvest " + inc.getPlant().getName());
            return; // don't touch inventory or clear incubator
        }

        // Safe to add everything now
        for (ItemStack stack : yield) {
            farm.getInv().add(stack.getItem(), stack.getQuantity());
        }

        farm.clearInc(inc);
        colonist.setStatus("Harvested " + inc.getPlant().getName());
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
        System.out.println("Plant planted");

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
        // Already have a pending delivery — just head to storage
        if (colonistam.getColonist().getInventory().hasDeliveries()) {
            System.out.println("off");
            deliverToStorage(farm);
            return;
        }

        if (!farm.getInv().claimTransport()) return;

        // Collect food into a delivery
        List<ItemStack> collected = new ArrayList<>();
        List<ItemStack> goods;
        if(!farm.getInv().hasType(ItemType.FOOD)&& isFarmNearFull(farm)){
            goods = new ArrayList<>(farm.getInv().getByType(ItemType.SEED));
        }
        else{
            goods = new ArrayList<>(farm.getInv().getByType(ItemType.FOOD));
        }
        for (ItemStack stack : goods) {
            int toTake;
            if(stack.getItem().getType().equals(ItemType.FOOD)) {
                toTake = stack.getQuantity();
            }
            else{
                toTake = Math.min(stack.getQuantity(), 100);
            }
            if (farm.getInv().remove(stack.getItem(), toTake)) {
                collected.add(new ItemStack(stack.getItem(), toTake));
            }
        }

        if (!collected.isEmpty()) {
            Inventory storageInv = farm.getPreferredStorage() != null
                    ? farm.getPreferredStorage().getInv()
                    : null; // null destination means search is needed
            Delivery delivery = new Delivery(collected, farm.getInv(), storageInv);
            colonistam.getColonist().getInventory().addDelivery(delivery);
            colonist.setStatus("Transporting goods to storage...");
            deliverToStorage(farm);
        } else {
            farm.getInv().releaseTransportClaim();
        }
    }

    private void deliverToStorage(Farm farm) {
        var preferred = farm.getPreferredStorage();
        if (preferred != null) {
            colonistam.setDeliveryTarget(preferred);
        } else if (!colonistam.getSearching()) {
            colonist.setStatus("Carrying goods, looking for storage...");
            colonistam.searchFor(null, BuildingType.STORAGE); // null = delivery mode
        }
    }



    // -------------------------------------------------------------------------
    // Early leave
    // -------------------------------------------------------------------------
    @Override
    public boolean hasNothingLeftToDo() {
        if (!(colonistam.getCurrentTile().getBuilding() instanceof Farm farm)) return false;

        boolean hasSeedsSomewhere = (colonistam.getColonist().getInventory().hasType(ItemType.SEED)
                || farm.getInv().hasType(ItemType.SEED))&&(!farm.getFreeInc().isEmpty());
        boolean myIncNeedTend = myIncubators(farm).stream().anyMatch(PlantIncubater::needsTendingToday);
        boolean myIncMature   = myIncubators(farm).stream().anyMatch(PlantIncubater::canHarvest);
        boolean hasGoodsToMove = farm.getInv().hasAvailableType(ItemType.FOOD)
                || colonistam.getColonist().getInventory().hasType(ItemType.FOOD);
        boolean farmFull = isFarmNearFull(farm) && myIncubators(farm).stream().anyMatch(PlantIncubater::canHarvest);

        if(!hasSeedsSomewhere && !myIncNeedTend && !myIncMature && !hasGoodsToMove && !colonistam.getSearching() && !farmFull){
            System.out.println(colonist.getName() + " GO HOME");
        }


        return !hasSeedsSomewhere && !myIncNeedTend && !myIncMature && !hasGoodsToMove && !colonistam.getSearching() && !farmFull;
    }
}