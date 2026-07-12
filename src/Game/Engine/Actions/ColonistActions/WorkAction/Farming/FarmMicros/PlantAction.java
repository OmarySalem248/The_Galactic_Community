package Game.Engine.Actions.ColonistActions.WorkAction.Farming.FarmMicros;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.MicroAction;
import Game.Engine.Colonist.ActionManager;

public class PlantAction extends MicroAction {
    public PlantAction(String name, ActionManager colonist) {
        super("planting", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
