package Game.Engine.Actions.ColonistActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Colonist;

public abstract class ColonistAction {
    protected String name;
    protected  ActionManager colonistam;
    protected Colonist colonist;

    public ColonistAction(String name,ActionManager colonist) {
        this.colonistam      = colonist;
        this.colonist = colonistam.getColonist();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Executes the action in the context of the colony.
     * Returns true if successful.
     */
    public abstract boolean  execute();
}
