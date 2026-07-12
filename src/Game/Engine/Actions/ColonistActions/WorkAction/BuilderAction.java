package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Colonist.ActionManager;

public class BuilderAction extends WorkAction{
    public BuilderAction(ActionManager colonist) {
        super("Building", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public void updateQueue() {

    }
}
