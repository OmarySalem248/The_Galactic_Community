package Game.Engine.Colonist;

import Game.Engine.Actions.ColonistActions.MoveAction;
import Game.Engine.Actions.ColonistActions.WorkAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Map.Map;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;

public class ActionManager {
    private ColonistAvatar colonistav;

    private Colonist colonist;
    private GameEventBus eventBus;
    private Tile workTile;


    private Tile destination;
    public ActionManager(ColonistAvatar colonist, Tile startingTile, GameEventBus eventBus) {
        this.colonistav =colonist;
        this.colonist = colonistav.getColonist();
        this.destination = startingTile;
        this.eventBus = eventBus;

    }
    public Colonist getColonist() { return colonist; }
    public ColonistAvatar getAvatar() { return colonistav; }


    public void run(GameTime time, Map map, Tile location) {
        updateDestination();
        moveTowardDestination(map);
        if (colonistav.getStatus().getatWork() && time.minute()%10 == 0) {
            new WorkAction(this).execute();
        }
    }

    private void updateDestination() {

        if (colonistav.getStatus().getshouldWork()) {
            workTile = getFirstTile(this.colonist.getAssignedBuilding());
            if (workTile != null) destination = workTile;
        } else{
            Tile homeTile = getFirstTile(colonist.getDwelling());
            if (homeTile != null) destination = homeTile;
        }
    }
    private void moveTowardDestination(Map map) {
        Tile currentTile = getCurrentTile();
        if (currentTile == null || destination == null) return;
        if (currentTile == destination) return;
        new MoveAction(this, destination, map).execute();
    }

    private Tile getFirstTile(Building building) {
        if (building == null) return null;
        if (building.getCoords().isEmpty()) return null;
        return building.getCoords().get(0);
    }
    public Tile getCurrentTile(){
        return getAvatar().getCurrentTile();
    }


    public GameEventBus getEventBus() {
        return eventBus;
    }
}
