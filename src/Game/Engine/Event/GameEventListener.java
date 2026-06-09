package Game.Engine.Event;

public interface GameEventListener<T> {
    void onEvent(GameEvent<T> event);
}