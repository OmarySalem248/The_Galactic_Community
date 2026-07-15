package Game.Engine.Map;

import Game.Engine.Colonist.Memory.Search.*;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Map.Tiles.Tile;

/**
 * SearchTile.java
 * A virtual destination tile used when a colonist knows what they need
 * but not where to find it. Carries a Search query describing what to look for.
 *
 * MoveManager treats SearchTile specially — instead of pathfinding,
 * the colonist wanders using FOV until a matching building is spotted,
 * at which point the SearchTile resolves to a real Tile.
 *
 * Extends Tile with dummy coordinates (-1, -1) to signal virtual status.
 */
public class SearchTile extends Tile {

    private final Search search;
    private Tile resolvedTile = null;
    private int tilesExplored = 0;

    private static final int SEARCH_RADIUS = 20;
    private static final int MAX_TILES = SEARCH_RADIUS * SEARCH_RADIUS * 4;

    public SearchTile(Search search) {
        super(-1, -1); // virtual — not on the real map
        this.search = search;
    }

    public Search getSearch() { return search; }

    public boolean isResolved()        { return resolvedTile != null; }
    public Tile getResolvedTile()      { return resolvedTile; }
    public void resolve(Tile realTile) { this.resolvedTile = realTile; }

    /** Called each tick the colonist wanders — returns true if search radius exhausted. */
    public boolean incrementExplored() {
        tilesExplored++;
        return tilesExplored >= MAX_TILES;
    }



    public int getTilesExplored() { return tilesExplored; }
}