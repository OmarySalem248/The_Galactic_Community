package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.ActionManager;

public class FarmAction extends WorkAction {
    public FarmAction( ActionManager colonist) {
        super("Farming", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
