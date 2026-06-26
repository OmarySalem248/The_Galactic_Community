package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.FarmAction;
import Game.Engine.Actions.ColonistActions.WorkAction.*;
import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;

public class Farmer extends Profession {
    public Farmer() {
        super("Famer", FarmAction.class);
    }


    public String getName() { return "Farmer"; }

    @Override
    public boolean isItWorkHours(GameTime time){
        return true;
    }


}