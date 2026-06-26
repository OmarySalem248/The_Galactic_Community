package Game.Engine.Time;

import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Map;
import Game.Engine.Game;
import Game.Engine.Time.WorldEvent.WorldEventManager;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * GameClock.java
 * Drives the real-time simulation.
 * Each tick = 1 in-game minute.
 * Scheduled Tickables run only when due (TickScheduler), avatars run every tick,
 * TickListeners (UI/observers) fire last.
 */
public class GameClock {

    private static final int TICK_MS = 200; // real ms per in-game tick — tweak freely

    private final Map map;
    private WorldEventManager eman;

    private int weekday;
    private long tick;
    private int ticc;

    private final List<TickListener> listeners = new ArrayList<>();
    private final TickScheduler scheduler = new TickScheduler();

    private int minute = 0;
    private int hour = 8;
    private int day  = 1;

    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );
    private Game game;

    private final Timer timer;

    public GameClock(Game game) {
        this.game = game;
        this.eman = new WorldEventManager(game);
        this.map  = game.getMap();
        this.tick = 0;
        this.ticc = 0;
        this.timer = new Timer(TICK_MS, e -> tick());
        this.timer.setDelay(5);
    }

    public void start()    { timer.start(); }
    public void stop()     { timer.stop();  }
    public boolean isRunning() { return timer.isRunning(); }
    public Map getMap()    { return map; }
    public void tickOnce() { tick(); }
    public int getHour()   { return hour; }
    public int getDay()    { return day; }
    public int getWeekday() { return day % 7; }
    public int getMinute() { return minute; }
    public long getTick()  { return tick; }
    public int getTicc()   { return ticc; }

    public void addTickListener(TickListener l) { listeners.add(l); }

    public void schedule(Tickable target, long delay) {
        scheduler.schedule(target, tick, delay);
    }

    private void tick() {
        tick++;
        ticc++;
        minute++;
        if (minute >= 60) { minute = 0; hour++; }
        if (hour   >= 24) { hour   = 0; day++;  }
        GameTime time = new GameTime(minute, hour, day, getWeekday(), ticc);
        if(time.minute() == 0) {
            System.out.println(time.hour());
        }
        // Only run scheduled Tickables that are actually due this tick
        scheduler.runDue(tick, time);

        // Build conflict graph — group colonists that could interact or
        // race for the same resource onto the same thread
        List<ColonistAvatar> avatars = map.getAvatars();
        ConflictGraph graph = new ConflictGraph(avatars);

        // Dispatch each conflict group as a single task
        // — colonists within a group run sequentially (safe, no races)
        // — groups themselves run in parallel (fully independent)
        ArrayList<ColonistAvatar> done = new ArrayList<>();
        List<Future<?>> tasks = new ArrayList<>();
        for (List<ColonistAvatar> group : graph.getGroups()) {
            tasks.add(pool.submit(() -> {
                for (ColonistAvatar avatar : group) {
                    if(!done.contains(avatar)) {
                        avatar.tick(time, map);
                        done.add(avatar);
                    }
                }
            }));
        }

        // Wait for all groups to finish
        for (Future<?> f : tasks) {
            try { f.get(); } catch (Exception ignored) {}
        }

        for (TickListener l : listeners) l.onTick(time);
    }
}