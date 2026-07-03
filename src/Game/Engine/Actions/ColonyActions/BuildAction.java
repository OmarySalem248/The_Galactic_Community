package Game.Engine.Actions.ColonyActions;

import Game.Engine.Buildings.Building;
import Game.Engine.Colony;
import Game.Engine.Inventory.Resources;
import Game.Engine.Map.Tile;

public class BuildAction extends ColonyAction {
    private Building building;
    private Tile tile;

    public BuildAction(Building building, Tile tile) {
        super("Build " + building.getName());
        this.building = building;
        this.tile = tile;
    }

    @Override
    public boolean execute(Colony colony) {
        Resources res = colony.getResources();
            colony.addBuilding(building);
            tile.placeBuilding(building);
            building.setcoords(tile);
            return true;


    }

    public Building getBuilding() { return building; }
}
