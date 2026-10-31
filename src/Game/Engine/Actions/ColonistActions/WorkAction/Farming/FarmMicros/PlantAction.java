package Game.Engine.Actions.ColonistActions.WorkAction.Farming.FarmMicros;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.MicroAction;
import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Items.Plant.Plant;
import Game.Engine.Inventory.Items.Seed.Seed;

public class PlantAction extends MicroAction {

    private Farm farm;
    private  PlantIncubater inc;
    private Seed seed;
    public PlantAction(ActionManager colonist, PlantIncubater inc, Seed seed) {
        super("planting", colonist);
        this.inc = inc;
        this.seed = seed;
    }

    @Override
    public boolean execute() {
        System.out.println("plantin it up");
        inc.plantSeed(seed, colonistam.getEventBus());
        return true;
    }
}
