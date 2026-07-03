package Game.Engine.Buildings;

import Game.Engine.Colonist.Career.Profession.TribeLeader;
import Game.Engine.Inventory.Items.Resources.Stone;
import Game.Engine.Inventory.Items.Resources.Wood;

public class TribeCentre extends Building{
    public TribeCentre(){
        super("Tribe Center",1, TribeLeader.class,1000,BuildingType.GOVERNMENT);
        addNeededRes(new Wood(),10);
        addNeededRes(new Stone(),10);
    }

}
