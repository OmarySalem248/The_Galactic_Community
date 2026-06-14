package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.WoodCutAction;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Resources;

public class Woodcutter extends Profession{
    public Woodcutter() {
        super("WoodCutter", WoodCutAction.class);
    }


    public String getName() { return "Woodcutter"; }
}
