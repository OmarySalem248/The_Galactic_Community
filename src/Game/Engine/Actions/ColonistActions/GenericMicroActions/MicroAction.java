package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Actions.ColonistActions.ColonistAction;
import Game.Engine.Colonist.ActionManager;

public abstract class MicroAction extends ColonistAction {
    public MicroAction(String name, ActionManager colonist) {
        super(name, colonist);
    }


}
