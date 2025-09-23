package Game.Actions;

import Game.Buildings.Building;
import Game.Colony;
import Game.Resources;

public class BuildAction extends Action {
    private Building building;

    public BuildAction(Building building) {
        super("Build " + building.getName());
        this.building = building;
    }

    @Override
    public boolean execute(Colony colony) {
        Resources res = colony.getResources();
        if (res.getWood() >= building.getWoodCost()
                && res.getStone() >= building.getStoneCost()) {

            // Deduct resources
            res.addWood(-building.getWoodCost());
            res.addStone(-building.getStoneCost());

            // Add the new building
            colony.addBuilding(building);
            return true;
        }
        return false;
    }

    public Building getBuilding() {
        return building;
    }
}
