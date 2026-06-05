package Game.Engine.Map;


import Game.Engine.Buildings.Building;
import Game.Windows.BuildWindow;

/**
 * Tile.java
 * Represents a single cell on the map.
 */
public class Tile {

    public final int col, row;
    public Building building;

    public Tile(int col, int row) {
        this.col      = col;
        this.row      = row;
        this.building = null;

    }

    public boolean hasBuilding() {
        return building != null;
    }

    public void placeBuilding(Building building){
        this.building = building;
    }

    @Override
    public String toString() {
        return "Tile[" + col + "," + row + (hasBuilding() ? " " + building : "") + "]";
    }
}
