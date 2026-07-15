package Game.Engine.Actions.ColonistActions.GenericMacroActions;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.CheckAction;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.MoveAction;
import Game.Engine.Actions.Queue.QueuedAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.BuildingType;
import Game.Engine.Colonist.ActionManager;


import Game.Engine.Colonist.Memory.Search.ClassSearch;
import Game.Engine.Colonist.Memory.Search.Search;
import Game.Engine.Colonist.Memory.Search.SearchResult;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.MemoryMap;
import Game.Engine.Map.Tiles.BuildingSnapshot;
import Game.Engine.Map.Tiles.Coords;
import Game.Engine.Map.Tiles.MemoryTile;
import Game.Engine.Map.Tiles.Tile;
import Game.Engine.Colonist.Memory.FOVCalculator;

import javax.print.attribute.standard.Destination;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static Game.Engine.Buildings.BuildingType.STORAGE;

/**
 * Wanders the map looking for a building that contains items matching the search.
 * Does NOT pick up anything — just finds and reports.
 * Returns true (done) when found or when search radius exhausted.
 */
public class SearchAction extends MacroAction {

    private final Search search;
    private Tile foundTile = null;
    private int tilesExplored = 0;


    private BuildingSnapshot target;
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
        MemoryMap memoryMap = colonistam.getMemoryMap();
        updateTargetMemory(memoryMap);
        if(notAtTarget(current)) {

            if (memoryMap.getSnaps().containsKey(STORAGE)) {
                Collection<BuildingSnapshot> potentialStor = memoryMap.getSnaps().get(STORAGE).values();
                if (!potentialStor.isEmpty()) {
                    target = potentialStor.stream()
                            .filter(snap -> betterResources(snap.inventory()))
                            .max(Comparator.comparingLong(BuildingSnapshot::takenAtTick))
                            .orElse(null);
                }
            }
            if (target != null && !getMicroQueue().isQueued(MoveAction.class)) {
                queueAction(new QueuedAction(new MoveAction(colonistam, target.coords()), 10, true, true));
            } else {
                MemoryTile next = colonistam.getMemory().wanderUnexplored((MemoryTile) memoryMap.getTileByCoords(current.getCoords()));
                if (next != null && !getMicroQueue().isQueued(MoveAction.class)) {
                    queueAction(new QueuedAction(new MoveAction(colonistam, next.getCoords()), 30, false, true));
                }
            }
        }else{
            if(current.same(target.coords())){
                if(current.hasBuilding()){
                    if(current.getBuilding().getBType() == STORAGE){
                        Building building = current.getBuilding();
                        check(building,memoryMap);
                    }
                }
            }
        }















        if(memoryMap.findBuildingCoordsByType(STORAGE)!= null){

        }
        if(memoryMap.findBuildingCoordsByType(STORAGE) !=)
        for (MemoryTile tile : memoryMap.) {
            if (!tile.hasBuilding()) continue;
            if(tile.getBuilding().getBType() == STORAGE){
                queueAction(new QueuedAction(new MoveAction(colonistam,tile.getCoords()),10,true,true));
            }
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

        MemoryTile next = colonistam.getMemory().wanderUnexplored((MemoryTile) memoryMap.getTileByCoords(current.getCoords()));
        if (next != null) {
            queueAction(new QueuedAction(new MoveAction(colonistam,next.getCoords()),30,false,true));
        }

    }

    public void check(Building b,MemoryMap memoryMap){
        Inventory inv = b.getInv();
        Inventory inventorycopy = new Inventory();
        for(ItemStack stack: b.getInv().getStacks()){
            inventorycopy.add(stack.getItem(),stack.getQuantity());
        }
        BuildingSnapshot snapshot = new BuildingSnapshot(b.getName(), b.getBType(), b.getId(), inventorycopy, colonistam.getTime().tick(), b.getRealCoords());
        memoryMap.logSnap(snapshot);
    }


    private void updateTargetMemory(MemoryMap memoryMap) {
        if(!memoryMap.getSnaps().containsKey(target.type())){
            target = null;
        }
        if(!memoryMap.getSnaps().get(target.type()).containsKey(target.id())){
            target = null;
        }
        target = memoryMap.getSnaps().get(target.type()).get(target.id());
    }

    private boolean notAtTarget(Tile current) {
        if(target == null){
            return true;
        }
        return target.coords() != current.getCoords();

    }

    private boolean betterResources(Inventory inventory) {
        if(target == null){
            return true;
        }
        Inventory targetinv = target.inventory();
        float currentsuit = search.getSuitability(targetinv);
        float newsuit = search.getSuitability(inventory);
        return newsuit > currentsuit;
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