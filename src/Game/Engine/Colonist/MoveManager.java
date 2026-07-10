package Game.Engine.Colonist;



import Game.Engine.Colonist.Memory.ColonistMemory;
import Game.Engine.Colonist.Memory.FOVCalculator;
import Game.Engine.Map.GameMap;
import Game.Engine.Map.SearchTile;
import Game.Engine.Map.Tile;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * MoveManager.java
 * Owned by each colonist — handles all movement logic independently of action decisions.
 * ActionManager sets destination, MoveManager figures out how to get there.
 */
public class MoveManager {

    private final ColonistAvatar avatar;
    private final ColonistMemory memory;
    private final GameMap map;

    private Tile destination;
    private final Deque<Tile> path = new ArrayDeque<>(); // FIFO path queue

    public MoveManager(ColonistAvatar avatar, ColonistMemory memory, GameMap map) {
        this.avatar  = avatar;
        this.memory  = memory;
        this.map     = map;
    }

    /** Called by ActionManager when the desired destination changes. */
    public void changeDes(Tile destination) {
        if (this.destination == destination) return; // already heading there
        this.destination = destination;
        path.clear(); // invalidate old path
        calculatePath(destination);
    }

    /**
     * Builds a path toward destination using known memory.
     * Uses FOV-aware greedy pathfinding — prefers explored tiles,
     * falls back to wandering toward unexplored when stuck.
     * Called again automatically when path is exhausted but destination not reached.
     */
    public void calculatePath(Tile destination) {
        path.clear();
        if (destination == null || avatar.getCurrentTile() == null) return;

        Tile current = avatar.getCurrentTile();

        // Simple greedy step-toward using memory — upgrade to A* later if needed
        int col = current.col;
        int row = current.row;
        int destCol = destination.col;
        int destRow = destination.row;

        // Build a path of intermediate tiles toward destination
        while (col != destCol || row != destRow) {
            if (col != destCol) col += (destCol > col) ? 1 : -1;
            else if (row != destRow) row += (destRow > row) ? 1 : -1;

            Tile next = map.getTile(col, row);
            if (next == null) break;
            path.add(next);
        }
    }

    /**
     * Called once per tick by ActionManager after priorities are evaluated.
     * Handles SearchTile destinations specially — wanders via FOV rather than pathfinding.
     */
    public void move() {
        if (destination == null) return;

        // Handle virtual SearchTile destination
        if (destination instanceof SearchTile searchTile) {
            handleSearchTile(searchTile);
            return;
        }

        if (avatar.getCurrentTile() == destination) return;

        if (path.isEmpty()) {
            calculatePath(destination);
            if (path.isEmpty()) return;
        }

        Tile next = path.poll();
        if (next != null) {
            avatar.getCurrentTile().colonistExit(avatar);
            avatar.setCurrentTile(next);
            next.colonistEnter(avatar);
        }
    }

    /**
     * Wanders toward unexplored tiles, checking FOV each tick for a matching building.
     * Resolves the SearchTile when found, or marks failure when radius exhausted.
     */
    private void handleSearchTile(SearchTile searchTile) {
        // Check if already resolved
        if (searchTile.isResolved()) {
            destination = searchTile.getResolvedTile();
            return;
        }

        // Scan visible tiles for a matching building
        List<Tile> visible = FOVCalculator.calculate(avatar.getCurrentTile(), 12, map);
        for (Tile tile : visible) {
            memory.observe(tile, null); // update memory with what we see
            if (tile.hasBuilding()) {
                var inv = tile.getBuilding().getInv();
                // Check if any item in the building matches the search
                boolean found = inv.getStacks().stream()
                        .anyMatch(s -> searchTile.getSearch().matches(s.getItem()) && s.getQuantity() > 0);
                if (found) {
                    searchTile.resolve(tile);
                    destination = tile;
                    return;
                }
            }
        }

        // Not found yet — wander toward unexplored
        boolean exhausted = searchTile.incrementExplored();
        if (exhausted) {
            // Signal failure back to ActionManager via a flag on the SearchTile
            searchTile.resolve(null); // null resolution = failure
            destination = null;
            return;
        }

        Tile next = memory.wanderUnexplored(avatar.getCurrentTile(), map);
        if (next != null) {
            path.clear();
            path.add(next);
            Tile step = path.poll();
            avatar.getCurrentTile().colonistExit(avatar);
            avatar.setCurrentTile(step);
            step.colonistEnter(avatar);
        }
    }

    public Tile getDestination() { return destination; }
    public boolean atDestination() { return avatar.getCurrentTile() == destination; }
    public boolean hasPath() { return !path.isEmpty(); }
}