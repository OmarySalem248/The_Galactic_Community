package Game.Engine.Actions.ColonistActions;

public abstract class ColonistAction {
    protected String name;

    public ColonistAction(String name) {
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
