package Game.Engine.Buildings;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Event.ScheduleRequest;
import Game.Engine.Inventory.Items.Plant.Plant;
import Game.Engine.Inventory.Items.Seed.Seed;
import Game.Engine.Time.Tickable;

public class PlantIncubater implements Tickable {

    private static final long TEND_INTERVAL_TICKS = 60;
    private static final long DEATH_GRACE_TICKS   = 1440;
    private static final long DAY_TICKS           = 1440;

    private Plant plant;
    private Colonist assignedFarmer = null;

    private long ticksSinceLastTend = 0;
    private long ticksSinceDayStart = 0;
    private int tendsDue = 0;

    private boolean dead = false;

    public PlantIncubater() {}

    public void plantSeed(Seed seed, GameEventBus eventBus, Colonist farmer) {
        this.plant = seed.getPlant();
        this.plant.beginGrowth();
        this.ticksSinceLastTend = 0;
        this.ticksSinceDayStart = 0;
        this.tendsDue = plant.getDailyMain();
        this.dead = false;
        this.assignedFarmer = farmer;
        eventBus.fire(new GameEvent<>(GameEventType.SCHEDULE_TICKABLE,
                new ScheduleRequest(this, plant.getDelay())));
    }

    @Override
    public long tick() {
        if (dead || plant == null) return -1;

        ticksSinceLastTend += TEND_INTERVAL_TICKS;
        ticksSinceDayStart += TEND_INTERVAL_TICKS;
        plant.tick();


        // Day rollover — reset daily tend allowance
        if (ticksSinceDayStart >= DAY_TICKS) {
            ticksSinceDayStart -= DAY_TICKS;
            tendsDue = plant.getDailyMain();
        }

        // Death purely time-based — unassigned plants keep growing
        long overdueBy = ticksSinceLastTend - plant.getDelay();
        if (overdueBy >= DEATH_GRACE_TICKS) {
            kill();
            return -1;
        }

        return TEND_INTERVAL_TICKS;
    }

    public boolean tendto() {
        if (dead || plant == null) return false;
        if (!needsTending()) return false;
        tendsDue--;
        ticksSinceLastTend = 0;
        return true;
    }

    public boolean needsTending() {
        return !dead && plant != null
                && ticksSinceLastTend >= plant.getDelay()
                && tendsDue > 0;
    }

    public boolean canHarvest() {
        return plant != null && plant.isMatured();
    }

    public boolean isOverdue() {
        return ticksSinceLastTend > plant.getDelay();
    }

    // Assignment
    public void assign(Colonist farmer)  { this.assignedFarmer = farmer; }
    public void unassign()                     { this.assignedFarmer = null; }
    public Colonist getAssignedFarmer()  { return assignedFarmer; }
    public boolean isAssigned()                { return assignedFarmer != null; }
    public boolean isAssignedTo(Colonist farmer) { return assignedFarmer == farmer; }

    public boolean isDead()  { return dead; }
    public boolean isEmpty() { return plant == null; }
    public Plant getPlant()  { return plant; }

    private void kill() {
        dead  = true;
        plant = null;
    }

    public void clear() {
        plant = null;
        assignedFarmer = null;
        ticksSinceLastTend = 0;
        ticksSinceDayStart = 0;
        tendsDue = 0;
        dead = false;
    }
}