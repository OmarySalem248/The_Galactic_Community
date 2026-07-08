package Game.Engine.Buildings;

import Game.Engine.Colonist.Career.Profession.Miner;
import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;

public class Mine extends Building{
    public Mine() {

        super("Mine",2, Miner.class,500,BuildingType.WORKPLACE);

    }
    @Override
    public void setNeededRes() {
        addNeededRes(new Wood(),2);
        addNeededRes(new Stone(),5);
    }

}
