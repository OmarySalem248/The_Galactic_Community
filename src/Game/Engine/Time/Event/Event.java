package Game.Engine.Time.Event;

import Game.Engine.Colony;
import Game.Engine.Game;

public abstract class Event {
    protected String name;

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public abstract void execute(Game game);
}
