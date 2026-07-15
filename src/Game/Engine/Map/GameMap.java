package Game.Engine.Map;

import Game.Engine.Buildings.*;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;
import Game.Engine.Inventory.Items.Seed.UberrySeed;
import Game.Engine.Inventory.Items.Consumable.Food.UtopiaBar;
import Game.Engine.Map.Tiles.Coords;
import Game.Engine.Map.Tiles.MemoryTile;
import Game.Engine.Map.Tiles.Tile;

import javax.swing.plaf.PanelUI;
import java.util.*;




public class GameMap {

    // -----------------------------------------------------------------------
    // Map data
    // -----------------------------------------------------------------------
    public int    cols;
    public int rows;
    private List<ColonistAvatar> avatars;
    Tile[][] grid;   // grid[row][col]
    private ArrayList<Building> buildings;


    /** Create a blank map filled with PLAINS. */
    public GameMap(int cols, int rows) {
        this.buildings = new ArrayList<>();
        this.avatars = new ArrayList<>();
        this.cols = cols;
        this.rows = rows;
        this.grid = new Tile[rows][cols];
        popMap();
    }


    public void popMap(){
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Tile(c, r);
    }

    /** Create a map from an existing 2-D tile array [row][col]. */
    public GameMap(Tile[][] grid) {
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.grid = grid;
    }

    public static GameMap getBasicMap() {
        GameMap basicMap =  new GameMap(35,25);
        basicMap.placeBuilding(new Farm(),2,2);
        basicMap.placeBuilding(new LumberMill(),7,2);
        basicMap.placeBuilding(new Mine(),8,3);
        basicMap.placeBuilding(new EngineeringHub(),12,3);
        Storage beginnerSt = new Storage();
        beginnerSt.getInv().add(new UberrySeed(),100);
        beginnerSt.getInv().add(new UtopiaBar(),7);
        beginnerSt.getInv().add(new Wood(),10000);
        beginnerSt.getInv().add(new Stone(),10000);
        basicMap.placeBuilding(beginnerSt,1,7);
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
    public Tile findSpawn() {
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();

        Tile start = getTile(18, 13);
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            if (current.isEmpty()) return current;
            for (Tile n : getNeighbours(current)) {
                if (visited.add(n)) queue.add(n);
            }
        }
        return null;
    }

    public List<Tile> getNeighbours(Tile tile) {
        List<Tile> neighbours = new ArrayList<>();
        int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] offset : offsets) {
            Tile t = this.getTile(tile.col + offset[0], tile.row + offset[1]);
            if (t != null) neighbours.add(t);
        }
        return neighbours;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }


    public void moveAvatar(ColonistAvatar avatar, MemoryTile tile) {
        Tile realTile = locate(tile);
        avatar.getCurrentTile().colonistExit(avatar);
        tile.colonistEnter(avatar);
        avatar.setCurrentTile(tile);
    }

    public void colonistCastRay(List<Tile> visible, Tile origin, int dx, int dy) {
        List<Tile> ray = castRay(origin, dx, dy);
        visible.addAll(ray);
    }
    public List<Tile> castRay(Tile origin, int dx, int dy) {
        List<Tile> ray = new ArrayList<>();

        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        for (int i = 1; i <= steps; i++) {
            int col = origin.col + Math.round((float) dx * i / steps);
            int row = origin.row + Math.round((float) dy * i / steps);

            Tile tile = getTile(col, row);
            if (tile == null) break;

            ray.add(tile);

            if (tile.blocksVision()) break; // include blocker but stop here
        }

        return ray;
    }

    public void giveInteractTiles(ActionManager actionManager, Tile origin) {

        ArrayList<Tile> tiles = new ArrayList();
        for (int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++){
                int col = origin.col + x;
                int row = origin.row + y;

                Tile tile = getTile(col, row);
                if (tile == null) break;

                tiles.add(tile);

                if (tile.blocksVision()) break; // include blocker but stop here
            }

        }
        actionManager.setInteractable(tiles);

    }

    public Tile getTileFromCoords(Coords coords){
        return getTile(coords.x(), coords.y());
    }
    public Coords findBuildingCoordsByType(BuildingType type){
        for(Building building: buildings){
            if(building.getBType() == type){
                return building.getRealCoords();
            }
        }
        return null;
    }
    public Tile locate(MemoryTile memoryTile){
        return getTile(memoryTile.col,memoryTile.row);
    }

    public void popNeighbours(Tile origin, List<Tile> neighbours) {
        neighbours = getNeighbours(origin);
    }

    public Tile getTileByCoords(Coords coords) {
        return getTile(coords.x(),coords.y());
    }
}