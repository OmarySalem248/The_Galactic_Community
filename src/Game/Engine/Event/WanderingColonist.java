package Game.Engine.Event;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Colony;

public class WanderingColonist extends Event{
    private final Colonist c;
    public WanderingColonist(Colonist c) {
        super("Wandering");
        this.c = c;
    }

    @Override
    public void execute(Colony colony) {
        colony.addColonist(c);

    }
}
