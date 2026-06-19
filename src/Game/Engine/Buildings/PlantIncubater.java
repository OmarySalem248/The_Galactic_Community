package Game.Engine.Buildings;

import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Event.ScheduleRequest;
import Game.Engine.Inventory.Items.Plant;
import Game.Engine.Inventory.Items.Seed;
import Game.Engine.Time.Tickable;

public class PlantIncubater implements Tickable {

    private static final long TEND_INTERVAL_TICKS = 60;       // 1 hour between tends
    private static final long DEATH_GRACE_TICKS   = 1440;     // 24 hours overdue before death

    private Plant plant;
    private long ticksSinceLastTend = 0;
    private boolean dead = false;

    public PlantIncubater() {}

    public void plantSeed(Seed seed, GameEventBus eventBus) {
        this.plant = seed.getPlant();
        this.plant.beginGrowth();
        this.ticksSinceLastTend = 0;
        this.dead = false;
        eventBus.fire(new GameEvent<>(GameEventType.SCHEDULE_TICKABLE, new ScheduleRequest(this, 60)));
    }

    /**
     * Called by the TickScheduler once per TEND_INTERVAL_TICKS.
     * Returns the delay until the next check — negative to stop scheduling
     * (e.g. once dead and empty, no need to keep checking).
     */
    @Override
    public long tick() {
        if (dead || plant == null) return -1; // nothing to track, stop scheduling

        ticksSinceLastTend += TEND_INTERVAL_TICKS;

        long overdueBy = ticksSinceLastTend - TEND_INTERVAL_TICKS;
        if (overdueBy >= DEATH_GRACE_TICKS) {
            kill();
            return -1;
        }

        return TEND_INTERVAL_TICKS; // check again in another hour
    }

    /** Tend the plant — resets the timer. Returns true if tending was needed/useful. */
    public boolean tendto() {
        if (dead || plant == null) return false;
        if (!needsTending()) return false;
        ticksSinceLastTend = 0;
        return true;
    }

    public boolean needsTending() {
        return !dead && plant != null && ticksSinceLastTend >= TEND_INTERVAL_TICKS;
    }

    public boolean isOverdue() {
        return ticksSinceLastTend > TEND_INTERVAL_TICKS;
    }

    public boolean isDead()  { return dead; }
    public boolean isEmpty() { return plant == null; }

    private void kill() {
        dead  = true;
        plant = null;
    }

    public Plant getPlant() { return plant; }
}