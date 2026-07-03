package Game.Engine.Buildings;

import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;

public class Park extends  Building{
    public Park() {
        super("Park", Integer.MAX_VALUE, null,10,BuildingType.SOCIAL);
        addNeededRes(new Wood(),10);
        addNeededRes(new Stone(),10);
    }
}
