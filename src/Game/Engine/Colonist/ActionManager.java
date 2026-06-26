package Game.Engine.Colonist;

import Game.Engine.Actions.ColonistActions.*;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Inventory.Delivery;
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
    private final ColonistAvatar colonistav;
    private final Colonist colonist;
    private final GameEventBus eventBus;
    private final ColonistMemory memory;
    private final ActionQueue queue = new ActionQueue();

    private static final int PRIORITY_SEARCH = 15;
    private static final int PRIORITY_MOVE   = 10;
    private static final int PRIORITY_WORK   = 5;
    private static final int PRIORITY_SOCIAL = 3;
    private boolean workDone;

    private GameTime mentaltime;

    private Tile destination;
    private Tile workTile;

    // Search state
    private ItemType searchingFor = null;   // null = delivery mode (searching for building only)
    private BuildingType searchSource = null;

    public ActionManager(ColonistAvatar avatar, Tile startingTile, GameEventBus eventBus, Map map) {
        this.colonistav  = avatar;
        this.colonist    = avatar.getColonist();
        this.destination = startingTile;
        this.eventBus    = eventBus;
        this.memory      = new ColonistMemory();
        this.map         = map;
        this.workDone = false;
        this.mentaltime = null;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------
    public Colonist getColonist()     { return colonist; }
    public ColonistAvatar getAvatar() { return colonistav; }
    public ColonistStatus status()    { return colonistav.getStatus(); }
    public GameEventBus getEventBus() { return eventBus; }
    public Tile getCurrentTile()      { return colonistav.getCurrentTile(); }
    public Tile getDestination()      { return destination; }
    public ColonistMemory getMemory() { return memory; }
    public boolean getSearching()     { return status().getisSearching(); }

    // -------------------------------------------------------------------------
    // Main run loop
    // -------------------------------------------------------------------------
    public void run(GameTime time, Map map, Tile location) {
        this.mentaltime = time;
        if (!status().getIsAsleep()) {
            for (Tile tile : FOVCalculator.calculate(location, 12, map)) {
                memory.observe(tile, time.tick());
            }
            evaluatePriorities(time, map);
        }
        queue.tick();
    }

    // -------------------------------------------------------------------------
    // Priority evaluation — one clear pass, top to bottom
    // -------------------------------------------------------------------------
    private void evaluatePriorities(GameTime time, Map map) {

        // 1. Sleep — highest priority when at home and low energy
        if (status().getatHome() && (colonist.getEnergy() < 1000)
                && !queue.isQueued(SleepAction.class)) {
            queue.add(new QueuedAction(new SleepAction(this), Integer.MAX_VALUE, false, true));
            return; // nothing else matters while sleeping
        }

        // 2. Search arrival — check before movement so arrival is handled immediately
        if (status().getisSearching() && getCurrentTile() == destination) {
            handleSearchArrival();
        }

        // 3. Movement toward destination
        updateDestination();
        if (getCurrentTile() != destination && !queue.isQueued(MoveAction.class)) {
            queue.add(new QueuedAction(
                    new MoveAction(this, destination, map), PRIORITY_MOVE, false, true));
        }


        if (status().getshouldWork() && !getSearching()) {
            WorkAction currentWork = queue.getWork();
            //System.out.println(currentWork.hasNothingLeftToDo());
            if (currentWork != null && currentWork.hasNothingLeftToDo()) {
                Tile homeTile = getFirstTile(colonist.getDwelling());
                this.workDone = true;
                if (homeTile != null) destination = homeTile;
            }
        }

        // 4. Work

        if (status().getshouldWork() && status().getatWork()
                && !queue.isQueued(WorkAction.class)) {
            System.out.println("worrrk");
            queueWorkAction();
        }

        // 5. Hunger — parallel, can eat while working
        if (colonist.getHunger() > 40 && !queue.isQueued(ConsumeAction.class)) {
            List<ItemStack> food = colonist.getInventory().getByType(ItemType.FOOD);
            if (food != null && !food.isEmpty()) {
                queue.add(new QueuedAction(
                        new ConsumeAction(this, (Consumable) food.get(0).getItem()),
                        PRIORITY_WORK, true, false));
                food.get(0).remove(1);
            }
        }




        // 7. Social — lowest priority, parallel
        checkSocialOpportunities(time);
    }

    // -------------------------------------------------------------------------
    // Search arrival — handles both item pickup and delivery deposit
    // -------------------------------------------------------------------------
    private void handleSearchArrival() {
        Tile current = getCurrentTile();
        if (current == null || !current.hasBuilding()) {
            // Arrived but no building here — wander further
            wanderAndContinue();
            return;
        }

        Building building = current.getBuilding();
        if (building.getBType() != searchSource) {
            // Wrong building type — keep wandering
            wanderAndContinue();
            return;
        }

        Inventory buildingInv = building.getInv();

        if (searchingFor != null) {
            // Item pickup mode — looking for a specific item type
            if (buildingInv.hasType(searchingFor)) {
                ItemStack stack = buildingInv.getByType(searchingFor).get(0);
                int qty = 1;
                queue.add(new QueuedAction(
                        new PickupAction(this, buildingInv, stack.getItem(), qty),
                        PRIORITY_SEARCH, false, false));

                if (stack.getQuantity() - qty <= 0 || colonist.getInventory().isFull()) {
                    clearSearch();
                }
            } else if (colonist.getInventory().hasType(searchingFor)) {
                // Already have what we need — search complete
                clearSearch();
            } else {
                // Right building type but no stock — keep wandering
                wanderAndContinue();
            }
        } else {
            // Delivery mode — arrived at storage, deposit all pending deliveries
            for (Delivery d : new ArrayList<>(colonist.getInventory().getDeliveries())) {
                if (d.getDestination() == null) {
                    d.setDestination(buildingInv);
                }
                if (d.getDestination() == buildingInv) {
                    queue.add(new QueuedAction(
                            new DepositDeliveryAction(this, d),
                            PRIORITY_SEARCH, false, false));
                }
            }
            clearSearch();
        }
    }

    private void wanderAndContinue() {
        destination = wanderUnexplored();
        queue.add(new QueuedAction(
                new MoveAction(this, destination, map), PRIORITY_SEARCH, false, true));
    }

    // -------------------------------------------------------------------------
    // Search initiation
    // -------------------------------------------------------------------------

    /** Search for a specific item type at a specific building type. */
    public void searchFor(ItemType itemType, BuildingType source) {
        this.searchingFor = itemType;
        this.searchSource = source;
        status().setIsSearching(true);
        colonist.setStatus("Searching for " + (itemType != null ? itemType.name() : source.name()));

        Optional<Tile> known = memory.recall(source);
        if (known.isPresent() &&
                (itemType == null || known.get().getBuilding().getInv().hasType(itemType))) {
            destination = known.get();
        } else {
            destination = wanderUnexplored();
        }

        queue.add(new QueuedAction(
                new MoveAction(this, destination, map), PRIORITY_SEARCH, false, true));
    }

    /** Search for a storage building to deposit deliveries — no specific item needed. */
    public void searchForDelivery(BuildingType buildingType) {
        searchFor(null, buildingType);
    }

    public void clearSearch() {
        status().setIsSearching(false);
        searchingFor = null;
        searchSource = null;
    }

    // -------------------------------------------------------------------------
    // Destination management
    // -------------------------------------------------------------------------
    private void updateDestination() {
        if (status().getisSearching()) return; // search controls destination

        if (status().getshouldWork() && !workDone) {
            workTile = getFirstTile(colonist.getAssignedBuilding());
            if (workTile != null) destination = workTile;
        } else {
            Tile homeTile = getFirstTile(colonist.getDwelling());
            if (homeTile != null) destination = homeTile;
        }
    }

    public void setDeliveryTarget(Building storage) {
        Tile target = getFirstTile(storage);
        if (target != null) {
            destination = target;
            queue.add(new QueuedAction(
                    new MoveAction(this, destination, map), PRIORITY_SEARCH, false, true));
        }
    }

    // -------------------------------------------------------------------------
    // Social
    // -------------------------------------------------------------------------
    private void checkSocialOpportunities(GameTime time) {
        Tile current = getCurrentTile();
        if (current == null) return;

        for (ColonistAvatar other : current.getColonists()) {
            if (other == colonistav) continue;
            if (time.minute() == 0 && time.hour() % 2 == 0
                    && !queue.isQueued(ChitChatAction.class)) {
                queue.add(new QueuedAction(
                        new ChitChatAction(colonist, other.getColonist()),
                        PRIORITY_SOCIAL, true, true));
            }
            break;
        }
    }

    // -------------------------------------------------------------------------
    // Sleep
    // -------------------------------------------------------------------------
    public void sleep() { status().setSleep(true); }
    public void wake()  {
        status().setSleep(false);
        workDone = false;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private void queueWorkAction() {
        try {
            Class<? extends WorkAction> actionClass = colonist.getProfession().getWorkAction();
            WorkAction workAction = actionClass
                    .getDeclaredConstructor(ActionManager.class).newInstance(this);
            queue.add(new QueuedAction(workAction, PRIORITY_WORK, true, false));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Work action missing ActionManager constructor", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to queue work action", e);
        }
    }

    private Tile wanderUnexplored() {
        Random rand = new Random();
        Tile current = getCurrentTile();
        if (current == null) return destination;

        List<Tile> neighbours = current.getNeighbours(map);
        List<Tile> unexplored = new ArrayList<>();
        for (Tile n : neighbours) {
            if (!memory.hasSeen(n)) unexplored.add(n);
        }

        List<Tile> candidates = unexplored.isEmpty() ? neighbours : unexplored;
        if (candidates.isEmpty()) return destination;
        return candidates.get(rand.nextInt(candidates.size()));
    }

    private Tile getFirstTile(Building building) {
        if (building == null) return null;
        if (building.getCoords().isEmpty()) return null;
        return building.getCoords().get(0);
    }

    public GameTime getTime() {
        return mentaltime;
    }

    public ActionQueue getQueue() {
        return queue;
    }

    public boolean getWorkDone(){
        return workDone;
    }
}