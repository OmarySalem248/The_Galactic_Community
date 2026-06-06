package Game.Engine.Actions.ColonyActions;

import Game.Engine.Colony;

public abstract class ColonyAction{
    protected String name;
    public ColonyAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean execute(Colony colony);



}
