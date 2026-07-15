package Game.Engine.Colonist;

import Game.Engine.Map.Tiles.Tile;
import Game.Engine.Time.GameTime;
/*
Physical representation of colonist, the shell that contains data in Colonist class and decision making in ActionManager
 */
public class ColonistAvatar {
    private final Colonist colonist;
    private Tile currentTile;
    private  ActionManager actionManager;

    private ColonistStatus status;

    public ColonistAvatar(Colonist colonist, Tile startingTile) {
        this.colonist    = colonist;
        this.currentTile = startingTile;
        this.status = new ColonistStatus(this);
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public ColonistStatus getStatus(){
        return status;
    }

    public Colonist getColonist() { return colonist; }
    public Tile getCurrentTile()  { return currentTile; }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void tick(GameTime time) {
        if(colonist.getName().equals("Annie")){
            System.out.println("letsago");
        }
        actionManager.run(time,getCurrentTile());
        status.update(time);


    }
    public void setCurrentTile(Tile tile){

        this.currentTile = tile;
    }


}
