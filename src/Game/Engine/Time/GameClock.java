package Game.Engine.Time;



import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Map;
import Game.Engine.Game;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

/**
 * GameClock.java
 * Drives the real-time simulation.
 * Each tick = 1 in-game hour.
 * Manages ColonistAvatars and notifies listeners when the display needs updating.
 */
public class GameClock {

    public interface TickListener {
        void onTick(int hour, int day);
    }

    private static final int TICK_MS = 200; // real ms per in-game hour — tweak freely

    private final Map map;
    private final List<ColonistAvatar> avatars = new ArrayList<>();
    private final List<TickListener>   listeners = new ArrayList<>();

    private int hour = 6; // start at 6am
    private int day  = 1;
    private Game game;

    private final Timer timer;

    public GameClock(Game game) {
        this.game = game;
        this.map = game.getMap();
        this.timer = new Timer(TICK_MS, e -> tick());
    }

    public void start() { timer.start(); }
    public void stop()  { timer.stop();  }
    public boolean isRunning() { return timer.isRunning(); }
    public Map getMap() { return map; }
    public int getHour() { return hour; }
    public int getDay()  { return day;  }

    public void addAvatar(ColonistAvatar avatar)     { avatars.add(avatar); }
    public void removeAvatar(ColonistAvatar avatar)  { avatars.remove(avatar); }
    public List<ColonistAvatar> getAvatars()         { return avatars; }

    public void addTickListener(TickListener l)      { listeners.add(l); }

    private void tick() {
        // Advance time
        hour++;
        if (hour >= 24) {
            hour = 0;
            day++;
        }

        // Move all avatars
        for (ColonistAvatar avatar : avatars) {
            avatar.tick(hour, map);
        }

        // Notify listeners (UI repaint, game logic hooks etc.)
        for (TickListener l : listeners) {
            l.onTick(hour, day);
        }
    }
}