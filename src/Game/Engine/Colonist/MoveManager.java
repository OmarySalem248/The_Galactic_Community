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


    private Tile destination;
    private final Deque<Tile> path = new ArrayDeque<>(); // FIFO path queue

    public MoveManager(ColonistAvatar avatar, ColonistMemory memory) {
        this.avatar  = avatar;
        this.memory  = memory;

    }




    /**
     * Called once per tick by ActionManager after priorities are evaluated.
     * Handles SearchTile destinations specially — wanders via FOV rather than pathfinding.
     */

    /**
     * Wanders toward unexplored tiles, checking FOV each tick for a matching building.
     * Resolves the SearchTile when found, or marks failure when radius exhausted.
     */

    public Tile getDestination() { return destination; }
    public boolean atDestination() { return avatar.getCurrentTile() == destination; }
    public boolean hasPath() { return !path.isEmpty(); }
}