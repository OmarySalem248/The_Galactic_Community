package Game.Engine.Actions.ColonistActions.GenericMacroActions;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.CheckAction;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.CollectDeliveryAction;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.DepositDeliveryAction;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.MoveAction;
import Game.Engine.Actions.Queue.QueuedAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.BuildingType;
import Game.Engine.Colonist.ActionManager;


import Game.Engine.Colonist.Memory.Search.ClassSearch;
import Game.Engine.Colonist.Memory.Search.Search;
import Game.Engine.Colonist.Memory.Search.SearchResult;
import Game.Engine.Inventory.Delivery;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.MemoryMap;
import Game.Engine.Map.Tiles.BuildingSnapshot;
import Game.Engine.Map.Tiles.Coords;
import Game.Engine.Map.Tiles.MemoryTile;
import Game.Engine.Map.Tiles.Tile;
import Game.Engine.Colonist.Memory.FOVCalculator;

import javax.print.attribute.standard.Destination;
import java.util.*;

import static Game.Engine.Actions.ColonistActions.GenericMacroActions.SearchStage.DELIVERING;
import static Game.Engine.Buildings.BuildingType.STORAGE;


public class SearchAction extends MacroAction {

    private Delivery delivery;
    private int totalCollected = 0;
    private final Map<Class<? extends Item>, Integer> collectedPerClass = new HashMap<>();

    private final Search search;
    private Tile foundTile = null;
    private int tilesExplored = 0;
    private SearchStage stage = SearchStage.COLLECTING;


    private BuildingSnapshot target;
    private static final int MAX_TILES = 20 * 20 * 4;

    public SearchAction(ActionManager colonist, Search search) {
        super("Search", colonist);
        this.search = search;
        this.delivery = new Delivery();
    }

    public Tile getFoundTile() { return foundTile; }
    public boolean wasSuccessful() { return foundTile != null; }
    public Search getSearch() { return search; }

    public void setStage(SearchStage stage){
        this.stage = stage;
    }

    @Override
    public void updateQueue() {
        System.out.println(stage);
        switch (stage) {
            case COLLECTING:
                Tile current = colonistam.getCurrentTile();
                MemoryMap memoryMap = colonistam.getMemoryMap();
                updateTargetMemory(memoryMap);
                if (notAtTarget(current)) {

                    if (memoryMap.getSnaps().containsKey(STORAGE)) {
                        Collection<BuildingSnapshot> potentialStor = memoryMap.getSnaps().get(STORAGE).values();
                        if (!potentialStor.isEmpty()) {
                            for (BuildingSnapshot snap : potentialStor) {
                                if (target == null) {
                                    target = snap;
                                } else {
                                    if (snap.hasInventoryData()) {
                                        if (betterResources(snap.getInventory())) {
                                            target = snap;
                                        }
                                    }
                                }
                            }
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
                } else {
                    if (current.same(target.coords())) {
                        if (current.hasBuilding()) {
                            if (current.getBuilding().getBType() == STORAGE) {
                                var inv = current.getBuilding().getInv();

                                if(search.invMatches(inv) && !getMicroQueue().isQueued(CollectDeliveryAction.class)){
                                    queueAction(new QueuedAction(new CollectDeliveryAction(colonistam, delivery, inv, search),30,true,false));

                                }
                                List<ItemStack> matching = inv.getStacks().stream()
                                        .filter(s -> search.matches(s.getItem()) && s.getQuantity() > 0)
                                        .toList();

                                for (ItemStack stack : matching) {
                                    int toTake = Math.min(stack.getQuantity(), search.getQuantity() - totalCollected);
                                    if (toTake <= 0) break;
                                    inv.remove(stack.getItem(), toTake);
                                    delivery.addItems(stack.getItem(), toTake);
                                    totalCollected += toTake;
                                    collectedPerClass.merge(stack.getItem().getClass(), toTake, Integer::sum);
                                }

                                if (totalCollected >= search.getQuantity()){
                                    setStage(DELIVERING);
                                }


                            }
                        }
                    }
                }


                // Not found — wander one tile toward unexplored
                tilesExplored++;
                if (tilesExplored >= MAX_TILES) {
                    recordFailure();
                    setComplete(true);
                }
                break;
            case DELIVERING:
                if(delivery.getItems().isEmpty()){
                    System.out.println("done!");
                    setComplete(true);
                    return;
                }
                if (!colonistam.getCurrentTile().getCoords().equals(search.getDest()) ) {
                    if(!getMicroQueue().isQueued(MoveAction.class)) {
                        queueAction(new QueuedAction(new MoveAction(colonistam, search.getDest()), 30, false, true));
                    }
                }else{
                    Building build = colonistam.getCurrentTile().getBuilding();
                    if(build != null && !getMicroQueue().isQueued(DepositDeliveryAction.class)) {
                        queueAction(new QueuedAction(new DepositDeliveryAction(colonistam, delivery, build.getInv()),30,true,false));
                    }
                }


        }


    }






    private void updateTargetMemory(MemoryMap memoryMap) {
        if(target != null) {
            if (!memoryMap.getSnaps().containsKey(target.type())) {
                target = null;
            }
            if (!memoryMap.getSnaps().get(target.type()).containsKey(target.id())) {
                target = null;
            }
            target = memoryMap.getSnaps().get(target.type()).get(target.id());
        }

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
        if(!target.hasInventoryData()){
            return true;
        }
        Inventory targetinv = target.getInventory();

        float currentsuit = search.getSuitability(targetinv);
        float newsuit = search.getSuitability(inventory);
        return newsuit > currentsuit;
    }

    private void recordSuccess() {
        long tick = colonistam.getTime().tick();
        if (search instanceof ClassSearch cs) {
            colonistam.getMemory().recordClassSearch(cs.getItemClass(), SearchResult.SUCCESS, tick, foundTile);
        } else if (search instanceof Game.Engine.Colonist.Memory.Search.TypeSearch ts) {
            colonistam.getMemory().recordTypeSearch(ts.getType(), SearchResult.SUCCESS, tick, foundTile);
        }
    }

    private void recordFailure() {
        long tick = colonistam.getTime().tick();
        if (search instanceof ClassSearch cs) {
            colonistam.getMemory().recordClassSearch(cs.getItemClass(), SearchResult.FAILED, tick, null);
        } else if (search instanceof Game.Engine.Colonist.Memory.Search.TypeSearch ts) {
            colonistam.getMemory().recordTypeSearch(ts.getType(), SearchResult.FAILED, tick, null);
        }
    }
}