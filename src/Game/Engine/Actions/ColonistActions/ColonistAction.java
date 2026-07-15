package Game.Engine.Actions.ColonistActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Map.Tiles.Tile;

public abstract class ColonistAction {
    protected String name;
    protected  ActionManager colonistam;
    protected Colonist colonist;

    private Tile destination;

    public ColonistAction(String name,ActionManager colonist) {
        this.colonistam      = colonist;
        this.colonist = colonistam.getColonist();
        this.name = name;
        this.destination = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    /**
     * Executes the action in the context of the colony.
     * Returns true if successful.
     */
    public abstract boolean  execute();

    public Tile getDest() {
        return destination;
    }




    protected void setDes(Tile tile) {
        this.destination = tile;
    }
}
