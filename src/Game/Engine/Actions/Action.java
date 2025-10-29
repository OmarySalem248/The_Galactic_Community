package Game.Engine.Actions;

import Game.Engine.Colony;

public abstract class Action {
    protected String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Executes the action in the context of the colony.
     * Returns true if successful.
     */
    public abstract boolean  execute(Colony colony);
}