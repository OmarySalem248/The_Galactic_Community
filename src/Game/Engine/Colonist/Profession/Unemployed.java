package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Resources;

public class Unemployed extends Profession{
    public Unemployed() {
        super("Unemployed", null);
    }

    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0, 0);
    }
    public String getName() { return "Unemployed"; }
}
