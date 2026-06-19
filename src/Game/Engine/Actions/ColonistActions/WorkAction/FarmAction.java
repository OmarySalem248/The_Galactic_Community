package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Buildings.BuildingType;
import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Inventory.Items.Seed;

import java.util.ArrayList;
import java.util.List;

public class FarmAction extends WorkAction {

    public FarmAction(ActionManager colonist) {
        super("Farming", colonist);
    }

    @Override
    public boolean execute() {
        if (!(colonistam.getCurrentTile().getBuilding() instanceof Farm farm)) {
            return true;
        }

        // No seeds anywhere — go search for some
        boolean noSeeds = !colonistam.getColonist().getInventory().hasType(ItemType.SEED)
                && !farm.getInv().hasType(ItemType.SEED)
                && farm.getActiveInc().isEmpty();
        if (noSeeds && !colonistam.getSearching()) {
            colonistam.getColonist().setStatus("Out of seeds, searching for storage...");
            colonistam.searchFor(ItemType.SEED, BuildingType.STORAGE);
            return true;
        }

        // Only one action per tick — checked in priority order
        if (hasSeedsToDeposit()) {
            depositSeeds(farm);
            return false;
        }

        if (hasIncubatorsToTend(farm)) {
            tendIncubators(farm);
            return false;
        }
        if (canPlant(farm)) {
            plant(farm);
            return false;
        }

        return false; // nothing to do this tick
    }

    private boolean hasSeedsToDeposit() {
        return colonistam.getColonist().getInventory().hasType(ItemType.SEED);
    }

    private void depositSeeds(Farm farm) {
        List<ItemStack> seeds = new ArrayList<>(
                colonistam.getColonist().getInventory().getByType(ItemType.SEED)
        );
        for (ItemStack seed : seeds) {
            farm.getInv().add(seed.getItem(), seed.getQuantity());
            colonistam.getColonist().getInventory().remove(seed.getItem(), seed.getQuantity());
        }
    }

    private boolean hasIncubatorsToTend(Farm farm) {
        return farm.getActiveInc().stream().anyMatch(PlantIncubater::needsTending);
    }

    private void tendIncubators(Farm farm) {
        for (PlantIncubater inc : farm.getActiveInc()) {
            if (inc.needsTending()) {
                inc.tendto();
                colonist.modEnergy(-1);
                break; // tend one per tick — keeps it as a single action
            }
        }
    }

    private boolean canPlant(Farm farm) {
        return farm.getInv().hasType(ItemType.SEED);
    }

    private void plant(Farm farm) {
        Item seed = farm.getInv().getByType(ItemType.SEED).get(0).getItem();
        farm.getInv().remove(seed, 1);
        farm.plant((Seed) seed, colonistam.getEventBus());
    }
    @Override
    public boolean hasNothingLeftToDo() {
        if (!(colonistam.getCurrentTile().getBuilding() instanceof Farm farm)) return false;

        boolean hasSeedsSomewhere = colonistam.getColonist().getInventory().hasType(ItemType.SEED)
                || farm.getInv().hasType(ItemType.SEED);
        boolean hasGrowingPlants = farm.getActiveInc().stream().anyMatch(inc -> !inc.isEmpty());

        if (hasSeedsSomewhere || hasGrowingPlants) return false; // there's clearly something to do

        // No seeds and nothing growing — but have we actually tried looking?
        return colonistam.getMemory().knowsOf(BuildingType.STORAGE);
    }
}