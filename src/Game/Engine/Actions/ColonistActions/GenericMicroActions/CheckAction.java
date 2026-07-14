package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;

public class CheckAction extends MicroAction{
    public CheckAction(String name, ActionManager colonist) {
        super("Checking", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
