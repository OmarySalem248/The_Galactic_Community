package Game.Engine.Buildings;

import Game.Engine.Colonist.Career.Profession.Woodcutter;
import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;

public class LumberMill extends Building{
    public LumberMill() {

        super("LumberMill",2, Woodcutter.class, 500, BuildingType.WORKPLACE);
        addNeededRes(new Wood(),3);
        addNeededRes(new Stone(),3);
    }
}
