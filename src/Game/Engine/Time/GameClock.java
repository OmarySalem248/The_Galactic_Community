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
 * Each tick = 1 in-game hour.
 * Manages ColonistAvatars and notifies listeners when the display needs updating.
 */
public class GameClock {





    private static final int TICK_MS = 200; // real ms per in-game hour — tweak freely

    private final Map map;
    private WorldEventManager eman;
    private final List<TickListener>   listeners = new ArrayList<>();
    private int minute = 0;
    private int hour = 8; // start at 6am
    private int day  = 1;
    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );
    private Game game;

    private final Timer timer;

    public GameClock(Game game) {
        this.game = game;
        this.eman = new WorldEventManager(game);
        this.map = game.getMap();
        this.timer = new Timer(TICK_MS, e -> tick());
    }

    public void start() { timer.start(); }
    public void stop()  { timer.stop();  }
    public boolean isRunning() { return timer.isRunning(); }
    public Map getMap() { return map; }
    public void tickOnce()  { tick(); }
    public int getHour() { return hour; }
    public int getDay()  { return day;  }

    public int getMinute() {
        return minute;
    }

    public void addTickListener(TickListener l)      { listeners.add(l); }

    private void tick() {
        minute++;
        if (minute >= 60) { minute = 0; hour++; }
        if (hour  >= 24)  { hour  = 0; day++;  }
        GameTime time = new GameTime(minute, hour, day);
        List<Future<?>> tasks = new ArrayList<>();
        for (ColonistAvatar avatar : map.getAvatars()) {
            tasks.add(pool.submit(() -> avatar.tick(time, map)));
        }
        // Wait for all avatars to finish before notifying listeners
        for (Future<?> f : tasks) {
            try { f.get(); } catch (Exception ignored) {}
        }


        for (TickListener l : listeners) l.onTick(time);
    }
}