package Game.Engine.Colonist;

import Game.Engine.Actions.ColonistActions.*;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Actions.Interactions.ChitChatAction;
import Game.Engine.Actions.Queue.ActionQueue;
import Game.Engine.Actions.Queue.QueuedAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.BuildingType;
import Game.Engine.Colonist.Memory.ColonistMemory;
import Game.Engine.Colonist.Memory.FOVCalculator;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.*;
import Game.Engine.Inventory.Items.Consumable.Consumable;
import Game.Engine.Map.Map;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class ActionManager {

    private final Map map;
    private ColonistAvatar colonistav;

    private ColonistMemory memory;

    private Colonist colonist;
    private GameEventBus eventBus;
    private Tile workTile;
    private ItemType searchingFor = null;
    private  BuildingType searchSource = null;

    private final ActionQueue queue = new ActionQueue();

    private static final int PRIORITY_MOVE = 10;
    private static final int PRIORITY_WORK = 5;
    private static final int PRIORITY_SOCIAL = 3;
    private static final int PRIORITY_SEARCH = 15;
    private Tile destination;
    private Inventory pendingSourceInventory = null;


    public ActionManager(ColonistAvatar colonist, Tile startingTile, GameEventBus eventBus,Map map) {
        this.colonistav =colonist;
        this.colonist = colonistav.getColonist();
        this.destination = startingTile;
        this.eventBus = eventBus;
        this.memory = new ColonistMemory();
        this.map = map;

    }
    public Colonist getColonist() { return colonist; }
    public ColonistAvatar getAvatar() { return colonistav; }
    public ColonistStatus status(){
        return getAvatar().getStatus();
    }

    public void run(GameTime time, Map map, Tile location)  {


        if(!status().getIsAsleep()) {
            for (Tile tile : FOVCalculator.calculate(location, 12, map)) {
                memory.observe(tile, time.tick());
            }

            evaluatePriorities(time, map);

        }

        queue.tick();
    }
    private void evaluatePriorities(GameTime time, Map map)  {



        if(status().getatHome() && colonist.getEnergy() < 700 && !queue.isQueued(SleepAction.class)){

            System.out.print("new sleep");
            if (!queue.isQueued(SleepAction.class)) {
                queue.add(new QueuedAction(
                        new SleepAction(this),
                        Integer.MAX_VALUE, false, true
                ));
            }
        }
        // Movement — highest priority, not parallel, interruptible
        updateDestination();
        if (getCurrentTile() != destination) {
            if (!queue.isQueued(MoveAction.class)) {
                queue.add(new QueuedAction(
                        new MoveAction(this, destination, map),
                        PRIORITY_MOVE, false, true
                ));
            }
        }

        // Work — parallel (can happen alongside social), not interruptible

        if (colonistav.getStatus().getshouldWork() && colonistav.getStatus().getatWork() && !queue.isQueued(WorkAction.class)) {
            try {
                Class<? extends WorkAction> action = colonist.getProfession().getWorkAction();
                WorkAction workAction = action.getDeclaredConstructor(ActionManager.class).newInstance(this);

                queue.add(new QueuedAction(workAction, PRIORITY_WORK, true, false));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Work action missing ActionManager constructor: ", e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to queue work action", e);
            }
        }
        if (colonist.getHunger() > 40 && !queue.isQueued(ConsumeAction.class)) {
            List<ItemStack> food = colonist.getInventory().getByType(ItemType.FOOD);

            if(food != null){
                queue.add(new QueuedAction(
                        new ConsumeAction(this, (Consumable) food.get(0).getItem()),
                        PRIORITY_WORK, true, false
                ));
                food.get(0).remove(1);
        }
        }
        if(status().getisSearching() && colonistav.getCurrentTile() == destination){
            checkSearchArrival();


            if(destination.getBuilding() != null && destination.getBuilding().getBType() == searchSource){
                Inventory inv = destination.getBuilding().getInv();
                if (inv.hasType(searchingFor)) {
                    ItemStack stack = inv.getByType(searchingFor).get(0);
                    Item item = stack.getItem();

                    //int qty = Math.min(stack.getQuantity(), 10);
                    int qty = 1;
                    queue.add(new QueuedAction(new PickupAction(this, inv, item, qty), PRIORITY_WORK, true, false));
                    // Don't clear search yet — only clear once storage is empty or colonist inventory is full
                    if (stack.getQuantity() - qty <= 0 || colonist.getInventory().isFull()) {
                        clearSearch();
                    }
                }
                else{
                    if(colonist.getInventory().hasType(searchingFor)){
                        clearSearch();
                    }
                }
            }
        }
        if (status().getisSearching() && colonist.getInventory().hasType(ItemType.FOOD)) {
            Tile current = getCurrentTile();
            if (current != null && current.hasBuilding()
                    && current.getBuilding().getBType() == BuildingType.STORAGE) {
                Inventory storageInv = current.getBuilding().getInv();
                List<ItemStack> goods = new ArrayList<>(
                        colonist.getInventory().getByType(ItemType.FOOD)
                );
                for (ItemStack stack : goods) {
                    queue.add(new QueuedAction(
                            new DropAction(this, storageInv,colonist.getInventory(), stack.getItem(), stack.getQuantity()),
                            PRIORITY_WORK, true, false
                    ));
                }
            }
        }

        if (status().getshouldWork() && !getSearching()) {
            WorkAction currentWork = queue.getWork();
            if (currentWork != null && currentWork.hasNothingLeftToDo()) {
                Tile homeTile = getFirstTile(colonist.getDwelling());
                if (homeTile != null) destination = homeTile;
            }
        }



        // Social — parallel, interruptible
        checkSocialOpportunities(time);
    }

    /** Check nearby colonists for interaction opportunities. */
    private void checkSocialOpportunities(GameTime time) {
        Tile current = getCurrentTile();
        if (current == null) return;

        List<ColonistAvatar> others = current.getColonists();
        for (ColonistAvatar other : others) {
            if (other == colonistav) continue;
            // Only chitchat for now, other interactions will be refactored for this knew system
            if(time.minute() ==0 && time.hour()%2 == 0) {
                ChitChatAction chitchat = new ChitChatAction(colonist, other.getColonist());
                if (!queue.isQueued(ChitChatAction.class)) {
                    queue.add(new QueuedAction(chitchat, PRIORITY_SOCIAL, true, true));
                }
            }

            break; // one interaction at a time for now
        }
    }

    private void updateDestination() {

        if(!status().getisSearching()) {

            if (colonistav.getStatus().getshouldWork()) {

                workTile = getFirstTile(this.colonist.getAssignedBuilding());
                if (workTile != null) destination = workTile;
            } else {
                Tile homeTile = getFirstTile(colonist.getDwelling());
                if (homeTile != null) destination = homeTile;
            }
        }
    }
    private void moveTowardDestination(Map map) {
        Tile currentTile = getCurrentTile();
        if (currentTile == null || destination == null) return;
        if (currentTile == destination) return;
        new MoveAction(this, destination, map).execute();
    }

    private Tile getFirstTile(Building building) {
        if (building == null) return null;
        if (building.getCoords().isEmpty()) return null;
        return building.getCoords().get(0);
    }
    public Tile getCurrentTile(){
        return getAvatar().getCurrentTile();
    }


    public GameEventBus getEventBus() {
        return eventBus;
    }


    /**
     * Enter search state for a specific item type from a specific building type.
     * Called by work actions (e.g. FarmAction) when they need a resource.
     */
    public void searchFor(ItemType itemType, BuildingType source) {
        this.searchingFor = itemType;
        this.searchSource = source;
        status().setIsSearching(true);
        colonist.setStatus("looking for seeds");

        // Check memory first
        Optional<Tile> known = memory.recall(source);
        if (known.isPresent() && known.get().getBuilding().getInv().hasType(searchingFor)) {
            destination = known.get();

        } else {
            destination = wanderUnexplored();

        }

        queue.add(new QueuedAction(
                new MoveAction(this, destination, map),
                PRIORITY_SEARCH, false, true
        ));
    }

    public void searchForBuilding(BuildingType buildingType) {
        this.searchSource = buildingType;
        status().setIsSearching(true);
        colonist.setStatus("looking for storage");

        // Check memory first
        Optional<Tile> known = memory.recall(buildingType);
        if (known.isPresent()) {
            System.out.println("here!");
            destination = known.get();

        } else {
            destination = wanderUnexplored();

        }

        queue.add(new QueuedAction(
                new MoveAction(this, destination, map),
                PRIORITY_SEARCH, false, true
        ));
    }

    /** Check if we've arrived at the target building — if so pick up item and exit search. */
    private void checkSearchArrival() {
        Tile current = getCurrentTile();
        if (current == null) return;

        if (current.hasBuilding()
                && current.building.getBType() == searchSource
                && (current.building.getInv().hasType(searchingFor)|| searchingFor == null)) {

            // Find the first matching item stack and pick it up
            if (searchingFor != null) {
                current.building.getInv().getByType(searchingFor).stream().findFirst()
                        .ifPresent(stack -> queue.add(new QueuedAction(
                                new PickupAction(this, current.building.getInv(),
                                        stack.getItem(), stack.getQuantity()),
                                PRIORITY_SEARCH, false, false
                        )));

                clearSearch();
            }
            else{

            }
        }else if (current == destination) {
            // Arrived but not found here — wander to next unexplored tile
            destination = wanderUnexplored();
            queue.add(new QueuedAction(
                    new MoveAction(this, destination, map),
                    PRIORITY_SEARCH, false, true
            ));
        }
    }

    /** Pick a random tile the colonist hasn't explored yet, biased away from known tiles. */
    private Tile wanderUnexplored() {

        Random rand = new Random();
        Tile current = getCurrentTile();
        if (current == null) return destination;

        List<Tile> neighbours = current.getNeighbours(map);
        List<Tile> unexplored = new ArrayList<>();

        for (Tile n : neighbours) {
            if (!memory.hasSeen(n)) unexplored.add(n);
        }

        // Prefer unexplored, fall back to any neighbour
        List<Tile> candidates = unexplored.isEmpty() ? neighbours : unexplored;
        if (candidates.isEmpty()) return destination;
        return candidates.get(rand.nextInt(candidates.size()));
    }

    public void clearSearch() {
        status().setIsSearching(false);
        searchingFor  = null;
        searchSource  = null;
    }

    public ColonistMemory getMemory() {
        return memory;
    }

    public boolean getSearching() {
        return status().getisSearching();
    }

    public Tile getDestination() {
        return destination;
    }



    public void sleep() {
        status().setSleep(true);
    }

    public void wake() {
        status().setSleep(false);
    }

    public void setDeliveryTarget(Building storage) {
        Tile target = storage.getCoords().isEmpty() ? null : storage.getCoords().get(0);
        if (target != null) {
            this.destination = target;
            queue.add(new QueuedAction(new MoveAction(this, destination, map), PRIORITY_SEARCH, false, true));
        }
    }


    public void setPendingSourceInventory(Inventory inv) { this.pendingSourceInventory = inv; }
    public Inventory getPendingSourceInventory() { return pendingSourceInventory; }


}
