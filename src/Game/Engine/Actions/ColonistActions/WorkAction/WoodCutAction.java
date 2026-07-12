package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Colonist.ActionManager;

public class WoodCutAction extends WorkAction{
    public WoodCutAction(ActionManager colonist) {
        super("Cut Wood", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public void updateQueue() {

    }


}
