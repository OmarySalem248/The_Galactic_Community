package Game.Engine.Actions.ColonistActions.GenericMacroActions;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.MoveAction;
import Game.Engine.Actions.Queue.QueuedAction;
import Game.Engine.Colonist.ActionManager;

package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Memory.Search.ClassSearch;
import Game.Engine.Colonist.Memory.Search.Search;
import Game.Engine.Colonist.Memory.Search.SearchResult;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.Tile;
import Game.Engine.Colonist.Memory.FOVCalculator;

import java.util.List;

/**
 * Wanders the map looking for a building that contains items matching the search.
 * Does NOT pick up anything — just finds and reports.
 * Returns true (done) when found or when search radius exhausted.
 */
public class SearchAction extends MacroAction {

    private final Search search;
    private Tile foundTile = null;
    private int tilesExplored = 0;
    private static final int MAX_TILES = 20 * 20 * 4;

    public SearchAction(ActionManager colonist, Search search) {
        super("Search", colonist);
        this.search = search;
    }

    public Tile getFoundTile() { return foundTile; }
    public boolean wasSuccessful() { return foundTile != null; }
    public Search getSearch() { return search; }



    @Override
    public void updateQueue() {
        Tile current = colonistam.getCurrentTile();


        // Scan FOV for a matching building with enough stock
        List<Tile> visible = FOVCalculator.calculate(current, 12, colonistam.getEventBus());
        for (Tile tile : visible) {
            if (!tile.hasBuilding()) continue;
            var stacks = tile.getBuilding().getInv().getStacks();
            int total = stacks.stream()
                    .filter(s -> search.matches(s.getItem()))
                    .mapToInt(ItemStack::getQuantity)
                    .sum();
            if (total >= search.getQuantity()) {
                foundTile = tile;
                recordSuccess();
                setComplete(true);
            }
        }

        // Not found — wander one tile toward unexplored
        tilesExplored++;
        if (tilesExplored >= MAX_TILES) {
            recordFailure();
            setComplete(true);
        }

        Tile next = colonistam.getMemory().wanderUnexplored(current, colonistam.getMap());
        if (next != null) {
            // Queue a move step via ActionManager's move manager
            queueAction(new QueuedAction(new MoveAction(colonistam,next),30,false,true));
        }

    }

    private void recordSuccess() {
        long tick = colonistam.getTime().tick();
        if (search instanceof ClassSearch cs) {
            colonistam.getMemory().recordClassSearch(cs.getItemClass(), SearchResult.SUCCESS, tick, foundTile);
        } else if (search instanceof Game.Engine.Colonist.Memory.TypeSearch ts) {
            colonistam.getMemory().recordTypeSearch(ts.getType(), SearchResult.SUCCESS, tick, foundTile);
        }
    }

    private void recordFailure() {
        long tick = colonistam.getTime().tick();
        if (search instanceof ClassSearch cs) {
            colonistam.getMemory().recordClassSearch(cs.getItemClass(), SearchResult.FAILED, tick, null);
        } else if (search instanceof Game.Engine.Colonist.Memory.TypeSearch ts) {
            colonistam.getMemory().recordTypeSearch(ts.getType(), SearchResult.FAILED, tick, null);
        }
    }
}