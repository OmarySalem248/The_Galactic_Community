package Game.Engine.Actions.Queue;

public class QueuedAction implements Comparable<QueuedAction> {

    private final Object action; // ColonistAction or InteractAction
    private int priority;
    private final boolean isParallel;
    private final boolean isInterruptible;

    public QueuedAction(Object action, int priority, boolean isParallel, boolean isInterruptible) {
        this.action          = action;
        this.priority        = priority;
        this.isParallel      = isParallel;
        this.isInterruptible = isInterruptible;
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
}