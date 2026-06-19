package Game.Engine.Time;

import java.util.PriorityQueue;

/**
 * Manages scheduled Tickables — only executes the ones actually due on a given tick.
 * Far more scalable than ticking everything every minute.
 */
public class TickScheduler {

    private final PriorityQueue<ScheduledTick> queue = new PriorityQueue<>();

    /** Schedule a Tickable to run 'delay' ticks from now. */
    public void schedule(Tickable target, long currentTick, long delay) {
        queue.add(new ScheduledTick(target, currentTick + delay));
    }

    /** Call every game tick — executes (and reschedules) anything due now. */
    public void runDue(long currentTick) {
        while (!queue.isEmpty() && queue.peek().getExecuteAtTick() <= currentTick) {
            ScheduledTick scheduled = queue.poll();
            long nextDelay = scheduled.getTarget().tick();

            if (nextDelay >= 0) {
                schedule(scheduled.getTarget(), currentTick, nextDelay);
            }
            // negative delay = Tickable wants to stop being scheduled, so we just drop it
        }
    }

    public int pendingCount() { return queue.size(); }
}