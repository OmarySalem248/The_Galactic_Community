package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Resources;

public class TribeLeader extends Leader{


    public TribeLeader() {
        super("Leader", null);
    }





    @Override
    public String getName() {
        return "Tribe Leader";
    }


}
