package Game.Engine.Buildings;

import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;

public class House extends Dwelling{
    public House() {

        super("House", 5, 5, Integer.MAX_VALUE,null,2, 400);

    }
    @Override
    public void setNeededRes() {
        addNeededRes(new Wood(),5);
        addNeededRes(new Stone(),5);

    }


}
