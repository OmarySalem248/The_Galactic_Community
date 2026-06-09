package Game.Engine.Actions.ColonistActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Event.ResourceProducedEvent;
import Game.Engine.Inventory.Resources;

public class WorkAction extends ColonistAction {

    public WorkAction(ActionManager colonist) {
        super("Work", colonist);
    }

    @Override
    public boolean execute() {

        Resources produced = colonist.work(colonist.getEnergy());

        if (produced != null) {
            colonistam.getEventBus().fire(new GameEvent<>(
                    GameEventType.RESOURCE_PRODUCED,
                    new ResourceProducedEvent(produced, colonist.getName())
            ));

        }
        return true;
    }
}