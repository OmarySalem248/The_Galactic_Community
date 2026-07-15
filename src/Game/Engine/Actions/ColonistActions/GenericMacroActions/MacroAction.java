package Game.Engine.Actions.ColonistActions.GenericMacroActions;

import Game.Engine.Actions.ColonistActions.ColonistAction;
import Game.Engine.Actions.Queue.ActionQueue;
import Game.Engine.Actions.Queue.QueuedAction;
import Game.Engine.Colonist.ActionManager;

public abstract class MacroAction extends ColonistAction {

    private ActionQueue microQueue;

    private boolean complete;
    public MacroAction(String name, ActionManager colonist) {

        super(name, colonist);
        this.microQueue = new ActionQueue();
        this.complete = false;
    }

    @Override
    public boolean execute(){
        updateQueue();
        microQueue.tick();
        return complete;
    }

    public ActionQueue getMicroQueue(){
        return microQueue;
    }

    public void setComplete(boolean complete){
        this.complete = complete;
    }

    public void queueAction(QueuedAction action){
        microQueue.add(action);

    }
    public abstract void updateQueue();
}
