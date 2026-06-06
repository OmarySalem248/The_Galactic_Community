package Game.Engine.Time;

public interface TickListener {
    void onTick(int min, int hour, int day);
}