package Game.Engine.Event;

import Game.Engine.Colony;

public abstract class Event {
    protected String name;

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public abstract void execute(Colony colony);
}
