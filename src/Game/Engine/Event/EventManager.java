package Game.Engine.Event;

import Game.Engine.Colony;
import Game.Engine.Game;

public class EventManager {
    private final Game game;
    private final GameEventBus eventBus;

    public EventManager(Game game, GameEventBus eventBus) {
        this.game     = game;
        this.eventBus = eventBus;
        subscribeAll();
    }

    private void subscribeAll() {
        eventBus.subscribe(GameEventType.RESOURCE_PRODUCED, event -> {
            ResourceProducedEvent rpe = (ResourceProducedEvent) event.data();
            game.getColony().getResources().add(rpe.produced());
        });


    }
}