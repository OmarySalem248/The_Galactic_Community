package Game.Engine.Actions.ColonistActions.WorkAction.Farming.FarmMicros;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.MicroAction;
import Game.Engine.Colonist.ActionManager;

public class TendAction extends MicroAction {
    public TendAction(String name, ActionManager colonist) {
        super("Tend", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
