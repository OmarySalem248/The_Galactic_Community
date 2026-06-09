package Game.Engine.Time.WorldEvent;

import Game.Engine.Colonist.ColonistGen;
import Game.Engine.Game;
import Game.Engine.Time.GameTime;
import Game.Engine.Time.TickListener;

import java.util.Random;

public class WorldEventManager implements TickListener {
    private Game game;
    private ColonistGen gen;

    private Random rand = new Random();
    public WorldEventManager(Game game) {
        this.game = game;
        this.gen = new ColonistGen(game.getColony());


    }



    @Override
    public void onTick(GameTime time) {
        if(time.day() >=7 && time.day() <= 12 && time.hour() == 0 && time.minute() == 0){
            WanderingColonist wanderingColonist = new WanderingColonist(this.gen.generate());
            wanderingColonist.execute(this.game);
        }
    }
}
