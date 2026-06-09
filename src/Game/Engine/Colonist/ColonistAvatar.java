package Game.Engine.Colonist;

import Game.Engine.Actions.ColonistActions.MoveAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Map.Map;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;
/*
Physical representation of colonist, the shell that contains data in Colonist class and decision making in ActionManager
 */
public class ColonistAvatar {
    private final Colonist colonist;
    private Tile currentTile;
    private  ActionManager actionManager;

    public ColonistAvatar(Colonist colonist, Tile startingTile) {
        this.colonist    = colonist;
        this.currentTile = startingTile;
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public Colonist getColonist() { return colonist; }
    public Tile getCurrentTile()  { return currentTile; }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void tick(GameTime time, Map map) {
        actionManager.run(time,map,getCurrentTile());
    }
    public void setCurrentTile(Tile tile){
        tile.colonistEnter(this);
        this.currentTile = tile;
    }


}
