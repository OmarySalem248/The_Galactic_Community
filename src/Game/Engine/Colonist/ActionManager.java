package Game.Engine.Colonist;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.ConsumeActions.ConsumeAction;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.*;

import Game.Engine.Actions.ColonistActions.SleepAction;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.DepositDeliveryAction;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.PickupAction;
import Game.Engine.Colonist.Memory.ToDo;
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
import Game.Engine.Map.GameMap;
import Game.Engine.Map.MemoryMap;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionManager {


    private final ColonistAvatar colonistav;
    private final Colonist colonist;
    private final GameEventBus eventBus;
    private final ColonistMemory memory;
    private final ActionQueue queue = new ActionQueue();
    private final MoveManager moveManager;

    private static final int PRIORITY_SEARCH = 15;
    private static final int PRIORITY_MOVE   = 10;
    private static final int PRIORITY_WORK   = 5;
    private static final int PRIORITY_SOCIAL = 3;

    private boolean workDone = false;
    private GameTime mentaltime = null;

    private MemoryMap memoryMap;

    // Search state
    private ItemType searchingFor = null;
    private BuildingType searchSource = null;

    public ActionManager(ColonistAvatar avatar, Tile startingTile, GameEventBus eventBus) {
        this.colonistav  = avatar;
        this.colonist    = avatar.getColonist();
        this.eventBus    = eventBus;
        this.memory      = new ColonistMemory();
        this.moveManager = new MoveManager(avatar, memory);
        memoryMap = new MemoryMap(startingTile);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------
    public Colonist getColonist()       { return colonist; }
    public ColonistAvatar getAvatar()   { return colonistav; }
    public ColonistStatus status()      { return colonistav.getStatus(); }
    public GameEventBus getEventBus()   { return eventBus; }
    public Tile getCurrentTile()        { return colonistav.getCurrentTile(); }
    public Tile getDestination()        { return moveManager.getDestination(); }
    public ColonistMemory getMemory()   { return memory; }
    public boolean getSearching()       { return status().getisSearching(); }
    public GameTime getTime()           { return mentaltime; }
    public ActionQueue getQueue()       { return queue; }
    public boolean getWorkDone()        { return workDone; }
    public MoveManager getMoveManager() { return moveManager; }

    // -------------------------------------------------------------------------
    // Main run loop
    // -------------------------------------------------------------------------
    public void run(GameTime time, Tile location) {
        if(colonist.getName().equals("Annie")){
            System.out.println("Sfdf");
        }
        this.mentaltime = time;
        memory.updateTime(mentaltime);

        if (!status().getIsAsleep()) {
            for (Tile tile : FOVCalculator.calculate(location, 12, eventBus)) {
                memory.observe(tile);
            }
            evaluatePriorities(time);

            // Top queued action drives destination
            var top = queue.peek();
            if (top != null && top.getDestination() != null) {
                moveManager.changeDes(top.getDestination());
            }
            moveManager.move();
        }
        if(queue.peek() != null){
            colonist.setStatus(queue.peek().toString());
        }else{
            colonist.setStatus("bored");
        }

        queue.tick();

    }

    // -------------------------------------------------------------------------
    // Priority evaluation
    // -------------------------------------------------------------------------
    private void evaluatePriorities(GameTime time) {


        // 1. Sleep
        if (status().getatHome() && colonist.getEnergy() < 1000
                && !queue.isQueued(SleepAction.class)) {
            Tile homeTile = getFirstTile(colonist.getDwelling());
            queue.add(new QueuedAction(
                    new SleepAction(this), Integer.MAX_VALUE, false, true));
            return;
        }


        // 3. Destination management
        updateDestination();

        // 4. Early leave check
        if (status().getshouldWork() && !getSearching() && !workDone) {
            WorkAction currentWork = queue.getWork();
            if (currentWork != null && currentWork.hasNothingLeftToDo()) {
                currentWork.setReminder(colonist.getAssignedBuilding());
                Tile homeTile = getFirstTile(colonist.getDwelling());
                workDone = true;
                if (homeTile != null) moveManager.changeDes(homeTile);
            }
        }

        // 5. Work
        if (status().getshouldWork()
                && !queue.isQueued(WorkAction.class) && !workDone) {
            if(colonist.getName().equals("Annie")){
                System.out.println("ERRFEF");
            }

            queueWorkAction();

        }


        // 6. Hunger
        if (colonist.getHunger() > 40 && !queue.isQueued(ConsumeAction.class)) {
            List<ItemStack> food = colonist.getInventory().getByType(ItemType.FOOD);
            if (food != null && !food.isEmpty()) {
                queue.add(new QueuedAction(
                        new ConsumeAction(this, (Consumable) food.get(0).getItem()),
                        PRIORITY_WORK, true, false));
                food.get(0).remove(1);
            }
        }

        // 7. Social
        checkSocialOpportunities(time);

    }

    // -------------------------------------------------------------------------
    // Search arrival
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Search initiation
    // -------------------------------------------------------------------------



    public void clearSearch() {
        status().setIsSearching(false);
        searchingFor = null;
        searchSource = null;
    }

    // -------------------------------------------------------------------------
    // Destination management
    // -------------------------------------------------------------------------
    private void updateDestination() {
        if (status().getisSearching()) return;

        // Check mental
        ToDo remember = memory.anyWorkToDo();
        if (remember != null) {
            workDone = false;
            moveManager.changeDes(remember.getDes());
            return;
        }

        if (status().getshouldWork() && !workDone) {
            Tile workTile = getFirstTile(colonist.getAssignedBuilding());
            if (workTile != null) moveManager.changeDes(workTile);
        } else {
            Tile homeTile = getFirstTile(colonist.getDwelling());
            if (homeTile != null) moveManager.changeDes(homeTile);
        }

    }

    public void setDeliveryTarget(Building storage) {
        Tile target = getFirstTile(storage);
        if (target != null) moveManager.changeDes(target);
    }

    public void setTileDestination(Tile tile) {
        if (tile != null) moveManager.changeDes(tile);
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
    public void wake()  { status().setSleep(false); }

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

    public Tile getFirstTile(Building building) {
        if (building == null) return null;
        if (building.getCoords().isEmpty()) return null;
        return building.getCoords().get(0);
    }

    public GameMap getMemoryMap() {
        return memory.getMemoryMap();
    }
}