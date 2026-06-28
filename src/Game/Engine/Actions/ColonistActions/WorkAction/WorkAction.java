package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Actions.ColonistActions.ColonistAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.Farm;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Event.ResourceProducedEvent;
import Game.Engine.Inventory.Resources;

public abstract class WorkAction extends ColonistAction {
    public WorkAction(String name, ActionManager colonist) {
        super(name, colonist);
    }

    public void setReminder(Building building){};

    public boolean hasNothingLeftToDo(){
        return false;
    };



/*
    public WorkAction(ActionManager colonist) {
        super("Work", colonist);
    }

    @Override
    public boolean execute() {

        Resources produced = colonist.work(colonist.getProductivity());
        colonist.modEnergy(-colonist.getEffort());

        if (produced != null) {
            colonistam.getEventBus().fire(new GameEvent<>(
                    GameEventType.RESOURCE_PRODUCED,
                    new ResourceProducedEvent(produced, colonist.getName())
            ));

        }
        return true;
    }

 */
}