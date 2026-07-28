package Game.Engine.Colonist.Career.Job;

import Game.Engine.Map.Tiles.Coords;
import Game.Engine.Map.Tiles.Tile;

public abstract class Job {
    private Tile location;

    public void setLocation(Tile location){
        this.location = location;
    }
    public Tile getLoc() {
        return location;
    }
    public Coords getLocCoords() {
        return location.getCoords();
    }
}
