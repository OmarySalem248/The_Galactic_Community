package Game.Engine.Actions.ColonistActions.WorkAction.Farming.FarmMicros;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.MicroAction;
import Game.Engine.Buildings.Farm;
import Game.Engine.Colonist.ActionManager;

public class PlantAction extends MicroAction {

    private Farm farm;
    public PlantAction(ActionManager colonist, Farm farm) {

        super("planting", colonist);
        this.farm = farm;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
