package Game.Engine.Colonist;

import Game.Engine.Actions.ColonistActions.ConsumeAction;
import Game.Engine.Actions.ColonistActions.MoveAction;
import Game.Engine.Actions.ColonistActions.SleepAction;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Actions.Interactions.ChitChatAction;
import Game.Engine.Actions.Queue.ActionQueue;
import Game.Engine.Actions.Queue.QueuedAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Inventory.Items.Consumable;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Map.Map;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;

import java.util.List;


public class ActionManager {
    private ColonistAvatar colonistav;

    private Colonist colonist;
    private GameEventBus eventBus;
    private Tile workTile;
    private final ActionQueue queue = new ActionQueue();

    private static final int PRIORITY_MOVE = 10;
    private static final int PRIORITY_WORK = 5;
    private static final int PRIORITY_SOCIAL = 3;
    private Tile destination;
    public ActionManager(ColonistAvatar colonist, Tile startingTile, GameEventBus eventBus) {
        this.colonistav =colonist;
        this.colonist = colonistav.getColonist();
        this.destination = startingTile;
        this.eventBus = eventBus;

    }
    public Colonist getColonist() { return colonist; }
    public ColonistAvatar getAvatar() { return colonistav; }
    public ColonistStatus status(){
        return getAvatar().getStatus();
    }

    public void run(GameTime time, Map map, Tile location)  {
        evaluatePriorities(time,map);
        queue.tick();
    }
    private void evaluatePriorities(GameTime time, Map map)  {
        if(status().getatHome() && colonist.getEnergy() < 500){
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

        if (colonistav.getStatus().getshouldWork() && colonistav.getStatus().getatWork() && time.minute()%10 == 0 && !queue.isQueued(WorkAction.class)) {
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

        if (colonistav.getStatus().getshouldWork()) {
            workTile = getFirstTile(this.colonist.getAssignedBuilding());
            if (workTile != null) destination = workTile;
        } else{
            Tile homeTile = getFirstTile(colonist.getDwelling());
            if (homeTile != null) destination = homeTile;
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
}
