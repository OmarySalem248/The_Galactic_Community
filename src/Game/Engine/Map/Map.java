package Game.Engine.Map;

import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.LumberMill;
import Game.Engine.Buildings.Mine;
import Game.Engine.Colonist.ColonistAvatar;

import java.util.ArrayList;
import java.util.List;

public class Map {

    // -----------------------------------------------------------------------
    // Map data
    // -----------------------------------------------------------------------
    public final int    cols, rows;
    private final List<ColonistAvatar> avatars = new ArrayList<>();
    private final Tile[][] grid;   // grid[row][col]
    private List<Building> buildings;

    /** Create a blank map filled with PLAINS. */
    public Map(int cols, int rows) {
        this.buildings = new ArrayList<>();
        this.cols = cols;
        this.rows = rows;
        this.grid = new Tile[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Tile(c, r);
    }

    /** Create a map from an existing 2-D tile array [row][col]. */
    public Map(Tile[][] grid) {
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.grid = grid;
    }

    public static Map getBasicMap() {
        Map basicMap =  new Map(34,24);
        basicMap.placeBuilding(new Farm(),1,2);
        basicMap.placeBuilding(new Farm(),2,2);
        basicMap.placeBuilding(new LumberMill(),7,2);
        basicMap.placeBuilding(new Mine(),8,3);
        return basicMap;
    }

    // -----------------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------------

    /** Returns the tile at (col, row), or null if out of bounds. */
    public Tile getTile(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) return null;
        return grid[row][col];
    }

    public void placeBuilding(Building building, int col, int row){
        this.buildings.add(building);
        getTile(col, row).placeBuilding(building);
    }
    public void addAvatar(ColonistAvatar avatar)    { avatars.add(avatar); }
    public void removeAvatar(ColonistAvatar avatar) { avatars.remove(avatar); }
    public List<ColonistAvatar> getAvatars()        { return avatars; }

    /** Returns true if (col, row) is within the map bounds. */
    public boolean inBounds(int col, int row) {
        return col >= 0 && col < cols && row >= 0 && row < rows;
    }


    public List<Building> getBuildings() {
        return buildings;
    }
}