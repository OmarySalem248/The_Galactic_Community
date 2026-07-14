package Game.Engine.Map;

import Game.Engine.Colonist.ColonistAvatar;

/**
 * MemoryMap.java
 * Per-colonist view of the game world.
 * Starts minimal — only spawn tile known, all others FogTile.
 * Grows and updates as the colonist observes their environment via FOV events.
 * This is what ActionManager and all actions interact with — never GameMap directly.
 */
public class MemoryMap extends GameMap {

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

        // Also ensure neighbours exist as FogTile (colonist knows they must exist)
        for (int[] offset : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
            int nc = realTile.col + offset[0];
            int nr = realTile.row + offset[1];
            if (nc >= 0 && nr >= 0) {
                ensureCapacity(nc, nr);
                if (grid[nr][nc] == null) {
                    grid[nr][nc] = new FogTile(nc, nr);
                }
            }
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

    public boolean isFog(int col, int row) {
        Tile t = getTile(col, row);
        return t == null || t instanceof FogTile;
    }
}