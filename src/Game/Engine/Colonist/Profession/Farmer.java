package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.FarmAction;
import Game.Engine.Actions.ColonistActions.WorkAction.*;

public class Farmer extends Profession {
    public Farmer() {
        super("Famer", FarmAction.class);
    }


    public String getName() { return "Farmer"; }


}