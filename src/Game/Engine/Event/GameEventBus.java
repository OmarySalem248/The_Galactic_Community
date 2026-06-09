package Game.Engine.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventBus {
    private final Map<GameEventType, List<GameEventListener<?>>> listeners = new HashMap<>();

    public <T> void subscribe(GameEventType type, GameEventListener<T> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void fire(GameEvent<T> event) {
        List<GameEventListener<?>> list = listeners.get(event.type());
        if (list == null) return;
        for (GameEventListener<?> listener : list) {
            ((GameEventListener<T>) listener).onEvent(event);
        }
    }
}