package Game.Engine.Buildings;

import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;

public class Cafe extends Building{
    public Cafe() {

        super("Cafe", Integer.MAX_VALUE, null,200,BuildingType.RESTRAUNT);
        addNeededRes(new Wood(),100);
        addNeededRes(new Stone(),50);
    }
}
