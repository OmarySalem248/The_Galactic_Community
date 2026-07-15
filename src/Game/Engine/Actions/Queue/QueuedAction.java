package Game.Engine.Actions.Queue;


import Game.Engine.Actions.ColonistActions.ColonistAction;
import Game.Engine.Map.Tiles.Tile;

public class QueuedAction implements Comparable<QueuedAction> {

    private ColonistAction action; // ColonistAction or InteractAction
    private int priority;
    private final boolean isParallel;
    private final boolean isInterruptible;

    private Tile destination;


    public QueuedAction(ColonistAction action, int priority, boolean isParallel, boolean isInterruptible) {
        this.action          = action;
        this.priority        = priority;
        this.isParallel      = isParallel;
        this.isInterruptible = isInterruptible;
        this.destination = getDestination();

    }

    public String toString(){
        return action.getName();
    }


    public Object getAction()        { return action; }
    public int getPriority()         { return priority; }
    public void setPriority(int p)   { this.priority = p; }
    public boolean isParallel()      { return isParallel; }
    public boolean isInterruptible() { return isInterruptible; }

    @Override
    public int compareTo(QueuedAction other) {
        // Higher priority number = executed first
        return Integer.compare(other.priority, this.priority);
    }

    public Tile getDestination() {
        return action.getDest();
    }
}