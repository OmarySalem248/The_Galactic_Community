package Game.Engine.Map;

import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.BuildingType;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.Tiles.*;

import java.util.*;

/**
 * MemoryMap.java
 * Per-colonist view of the game world.
 * Starts minimal — only spawn tile known, all others FogTile.
 * Grows and updates as the colonist observes their environment via FOV events.
 * This is what ActionManager and all actions interact with — never GameMap directly.
 */
public class MemoryMap extends GameMap {


    Map<BuildingType,Map<Integer,BuildingSnapshot>> snaps = new HashMap();

    public MemoryMap(Tile spawnTile) {
        // Start with minimum dimensions to contain the spawn tile
        super(spawnTile.col + 1, spawnTile.row + 1);

        // Fill everything with FogTile
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new FogTile(c, r);
            }
        }

        // Spawn tile is known — replace with a basic known tile
        grid[spawnTile.row][spawnTile.col] = spawnTile;
    }

    /**
     * Called when a real tile enters the colonist's FOV.
     * Upgrades FogTile → MemoryTile, or refreshes existing MemoryTile.
     * Expands grid dimensions if the observed tile is outside current bounds.
     */
    public synchronized void observe(Tile realTile, long tick) {
        ensureCapacity(realTile.col, realTile.row);

        Tile existing = grid[realTile.row][realTile.col];
        if (existing instanceof MemoryTile mt) {
            mt.update(realTile, tick);
        } else {
            grid[realTile.row][realTile.col] = new MemoryTile(realTile, tick);
        }
        MemoryTile updating =  (MemoryTile) grid[realTile.row][realTile.col];
        if (realTile.hasBuilding()) {
            Building b = realTile.getBuilding();
            Inventory inventorycopy = new Inventory();
            for(ItemStack stack: b.getInv().getStacks()){
                inventorycopy.add(stack.getItem(),stack.getQuantity());
            }
            BuildingSnapshot snapshot = new BuildingSnapshot(b.getName(), b.getBType(), b.getId(), null, tick, b.getRealCoords());
            updating.setSnap(snapshot);
            if(existing.getClass() == FogTile.class) {
                logSnap(snapshot);
            }else if (((MemoryTile) existing).getSnap() == null || ((MemoryTile) existing).getSnap().id() != realTile.getBuilding().getId() ){
                removeSnap(((MemoryTile) existing).getSnap());
                logSnap(snapshot);
            }


        }
        else{
            updating.setSnap(null);
            if (existing instanceof MemoryTile mt && mt.getSnap() != null) {
                removeSnap(mt.getSnap());
            }
        }

    }

    public void logSnap(BuildingSnapshot snap) {
        snaps.computeIfAbsent(snap.type(), k -> new HashMap<>())
                .put(snap.id(), snap);
    }

    public void removeSnap(BuildingSnapshot snap) {
        Map<Integer, BuildingSnapshot> typeMap = snaps.get(snap.type());
        if (typeMap != null) {
            typeMap.remove(snap.id());
        }
    }
    /**
     * Called when GameMap returns the 3x3 immediate surroundings.
     * These tiles are treated as freshly observed.
     */
    public synchronized void observeImmediate(Tile[] surroundings, long tick) {
        for (Tile t : surroundings) {
            if (t != null) observe(t, tick);
        }
    }

    /** Expand grid if observed tile is outside current dimensions. */
    private void ensureCapacity(int col, int row) {
        if (col < cols && row < rows) return;

        int newCols = Math.max(cols, col + 1);
        int newRows = Math.max(rows, row + 1);
        Tile[][] newGrid = new Tile[newRows][newCols];

        // Copy existing grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }

        // Fill new cells with FogTile
        for (int r = 0; r < newRows; r++) {
            for (int c = 0; c < newCols; c++) {
                if (newGrid[r][c] == null) {
                    newGrid[r][c] = new FogTile(c, r);
                }
            }
        }

        this.grid = newGrid;
        this.cols = newCols;
        this.rows = newRows;
    }

    @Override
    public MemoryTile getTile(int col, int row){
        if (isFog(col,row)) return null;
        if (col < 0 || col >= cols || row < 0 || row >= rows) return null;
        return (MemoryTile) grid[row][col];
    }

    @Override

    public Coords findBuildingCoordsByType(BuildingType type) {
        if (!snaps.containsKey(type)) return null;
        Map<Integer, BuildingSnapshot> typeMap = snaps.get(type);
        if (typeMap.isEmpty()) return null;
        return typeMap.values().stream()
                .max(Comparator.comparingLong(BuildingSnapshot::takenAtTick))
                .map(BuildingSnapshot::coords)
                .orElse(null);
    }

    public boolean isFog(int col, int row) {
        Tile t = getTile(col, row);
        return t == null || t instanceof FogTile;
    }

    public Map<BuildingType,Map<Integer, BuildingSnapshot>>  getSnaps() {
        return snaps;
    }
    @Override
    public void popNeighbours(Tile origin, List<Tile> neighbours) {
        neighbours = getNeighbours(origin);
    }
}