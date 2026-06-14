package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.BuilderAction;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Actions.ColonyActions.BuildAction;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Resources;

public class Builder extends Profession {


    public Builder() {
        super("Builder", BuilderAction.class);
    }

    @Override
    public String getName() {
        return "Builder";
    }
}
