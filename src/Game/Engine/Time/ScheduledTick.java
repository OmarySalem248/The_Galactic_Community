package Game.Engine.Time;

/**
 * Wraps a Tickable with the absolute tick count it should next execute at.
 */
public class ScheduledTick implements Comparable<ScheduledTick> {

    private final Tickable target;
    private final long executeAtTick;

    public ScheduledTick(Tickable target, long executeAtTick) {
        this.target = target;
        this.executeAtTick = executeAtTick;
    }

    public Tickable getTarget()     { return target; }
    public long getExecuteAtTick()  { return executeAtTick; }

    @Override
    public int compareTo(ScheduledTick other) {
        return Long.compare(this.executeAtTick, other.executeAtTick);
    }
}