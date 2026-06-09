package Game.Engine.Time.WorldEvent;

import Game.Engine.Game;

public abstract class WorldEvent {
    protected String name;

    public WorldEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public abstract void execute(Game game);
}
