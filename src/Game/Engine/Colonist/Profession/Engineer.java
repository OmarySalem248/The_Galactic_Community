package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Resources;

public class Engineer extends Profession{

    public Engineer(String name, Class<? extends WorkAction> workAction) {
        super(name, workAction);
    }

    @Override
    public String getName() {
        return null;
    }
}
