package Game.Engine.Colonist.Memory;

import Game.Engine.Map.Tiles.Tile;
import Game.Engine.Time.GameTime;

public class ToDo {
    private Tile where;

    private GameTime time;

    private TodoType type;

    public ToDo(Tile where, GameTime time, TodoType type){
        this.where = where;
        this.time = time;
        this.type = type;
    }

    public TodoType getType() {
        return type;
    }

    public GameTime getTime() {
        return time;
    }

    public Tile getDes() {
        return where;
    }
}
