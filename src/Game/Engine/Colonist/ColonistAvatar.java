package Game.Engine.Colonist;

import Game.Engine.Actions.ColonistActions.MoveAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Map.Map;
import Game.Engine.Map.Tile;

public class ColonistAvatar {
    private final Colonist colonist;
    private Tile currentTile;
    private Tile destination;

    public ColonistAvatar(Colonist colonist, Tile startingTile) {
        this.colonist    = colonist;
        this.currentTile = startingTile;
        this.destination = startingTile;
    }

    public Colonist getColonist() { return colonist; }
    public Tile getCurrentTile()  { return currentTile; }

    public void tick(int hour, Map map) {

        updateDestination(hour);
        moveTowardDestination(map);
    }

    private void updateDestination(int hour) {
        if (hour == 8) {
            Tile workTile = getFirstTile(colonist.getAssignedBuilding());
            if (workTile != null) destination = workTile;
        } else if (hour == 20) {
            Tile homeTile = getFirstTile(colonist.getDwelling());
            if (homeTile != null) destination = homeTile;
        }
    }
    private void moveTowardDestination(Map map) {
        if (currentTile == null || destination == null) return;
        if (currentTile == destination) return;
        System.out.print("movin");
        new MoveAction(this, destination, map).execute();
    }

    private Tile getFirstTile(Building building) {
        if (building == null) return null;
        if (building.getCoords().isEmpty()) return null;
        return building.getCoords().get(0);
    }

    public void setCurrentTile(Tile tile){
        tile.colonistEnter(this);
        this.currentTile = tile;
    }
}
