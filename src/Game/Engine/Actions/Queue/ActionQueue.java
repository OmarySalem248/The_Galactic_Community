package Game.Engine.Actions.Queue;

import Game.Engine.Actions.ColonistActions.ColonistAction;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Actions.Interactions.InteractAction;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ActionQueue {

    // Currently executing actions (parallel-capable)
    private final List<QueuedAction> active = new ArrayList<>();

    // Waiting to execute
    private final PriorityQueue<QueuedAction> pending = new PriorityQueue<>();

    public void add(QueuedAction action) {
        pending.add(action);
    }

    public void tick() {
        promoteFromPending();
        tickActive();
    }

    /** Move pending actions into active if slot is available. */
    private void promoteFromPending() {
        while (!pending.isEmpty()) {
            QueuedAction next = pending.peek();

            // Check if a non-interruptible non-parallel action is already running
            boolean blocked = active.stream()
                    .anyMatch(a -> !a.isParallel() && !a.isInterruptible());

            if (blocked && !next.isParallel()) break;

            // If higher priority interruptible action exists, bump it
            active.removeIf(a -> a.isInterruptible() && a.getPriority() < next.getPriority() && !next.isParallel());

            pending.poll();
            active.add(next);
        }
    }

    /** Tick all active actions, remove completed ones. */
    private void tickActive() {
        List<QueuedAction> completed = new ArrayList<>();
        for (QueuedAction qa : active) {
            boolean done = false;
            if (qa.getAction() instanceof ColonistAction ca) {
                done = ca.execute();
            } else if (qa.getAction() instanceof InteractAction ia) {
                done = ia.tick();
            }
            if (done) completed.add(qa);
        }
        active.removeAll(completed);
    }

    public boolean isQueued(Class<?> actionClass) {
        boolean inActive  = active.stream().anyMatch(q -> actionClass.isAssignableFrom(q.getAction().getClass()));
        boolean inPending = pending.stream().anyMatch(q -> actionClass.isAssignableFrom(q.getAction().getClass()));
        return inActive || inPending;
    }
    public void clear() {
        active.clear();
        pending.clear();
    }
    public QueuedAction peek() {
        if (!active.isEmpty()) return active.get(0);
        return pending.isEmpty() ? null : pending.peek();
    }

    public WorkAction getWork() {
        for (QueuedAction action : active) {
            if (action.getAction() instanceof WorkAction work) return work;
        }
        for (QueuedAction action : pending) {
            if (action.getAction() instanceof WorkAction work) return work;
        }
        return null;
    }
    public ArrayList<String>  print() {
        ArrayList<String> actions = new ArrayList<>();
        for (QueuedAction action : active) {
                actions.add(action.getAction().toString());
        }
        return actions;
    }
}