package Game.Engine.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventBus {
    private final Map<GameEventType, List<GameEventListener<?>>> listeners = new HashMap<>();

    public <T> void subscribe(GameEventType type, GameEventListener<T> listener) {  }
    public <T> void fire(GameEvent<T> event) {  }
}