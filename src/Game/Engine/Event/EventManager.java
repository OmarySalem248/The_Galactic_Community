package Game.Engine.Event;

import Game.Engine.Colonist.ColonistAvatar;
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
        eventBus.subscribe(GameEventType.SCHEDULE_TICKABLE, event -> {
            ScheduleRequest req = (ScheduleRequest) event.data();
            game.getClock().schedule(req.target(), req.delay());
        });
        eventBus.subscribe(GameEventType.COLONIST_MOVE, event -> {
            ColonistMoveEvent mov = (ColonistMoveEvent) event.data();
            game.getMap().moveAvatar( mov.avatar(),mov.to());
        });
        eventBus.subscribe(GameEventType.COLONIST_CASTRAY, event -> {
            ColonistCastRayEvent ray = (ColonistCastRayEvent) event.data();
            game.getMap().colonistCastRay( ray.visible() , ray.origin(), ray.dx(),ray.dy());
        });

    }
}