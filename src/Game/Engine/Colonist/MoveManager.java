package Game.Engine.Colonist;



import Game.Engine.Colonist.Memory.ColonistMemory;
import Game.Engine.Map.GameMap;
import Game.Engine.Map.Tile;

import java.util.ArrayDeque;
import java.util.Deque;

/**
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
     * Steps the avatar one tile along the path.
     */
    public void move() {
        if (destination == null) return;
        if (avatar.getCurrentTile() == destination) return;

        // Path exhausted but not at destination — recalculate
        if (path.isEmpty()) {
            calculatePath(destination);
            if (path.isEmpty()) return; // genuinely stuck
        }

        Tile next = path.poll();
        if (next != null) {
            avatar.getCurrentTile().colonistExit(avatar);
            avatar.setCurrentTile(next);
        }
    }

    public Tile getDestination() { return destination; }
    public boolean atDestination() { return avatar.getCurrentTile() == destination; }
    public boolean hasPath() { return !path.isEmpty(); }
}