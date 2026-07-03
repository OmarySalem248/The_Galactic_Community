package Game.Engine.Buildings;

import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;

public class Storage extends Building{

    public Storage() {

        super("Storage", Integer.MAX_VALUE, null,50000,BuildingType.STORAGE);
        addNeededRes(new Wood(),5);
        addNeededRes(new Stone(),5);
    }
}
