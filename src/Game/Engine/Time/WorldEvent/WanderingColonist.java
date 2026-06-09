package Game.Engine.Time.WorldEvent;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Game;

public class WanderingColonist extends WorldEvent {
    private final Colonist c;
    public WanderingColonist(Colonist c) {
        super("Wandering");
        this.c = c;
    }

    @Override
    public void execute(Game game) {
        game.addColonist(c,game.getMap().findSpawn());

    }
}
