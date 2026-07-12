package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;

public class SearchAction extends MicroAction{
    public SearchAction(String name, ActionManager colonist) {
        super("Searching", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
