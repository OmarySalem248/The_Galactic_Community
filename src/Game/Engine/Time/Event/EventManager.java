package Game.Engine.Time.Event;

import Game.Engine.Colonist.ColonistGen;
import Game.Engine.Game;
import Game.Engine.Time.TickListener;

import java.util.Random;

public class EventManager implements TickListener {
    private Game game;
    private ColonistGen gen;

    private Random rand = new Random();
    public EventManager(Game game) {
        this.game = game;
        this.gen = new ColonistGen(game.getColony());


    }



    @Override
    public void onTick(int min,int hour, int day) {
        if(day >=7 && day <= 12 && hour == 0 && min == 0){
            WanderingColonist wanderingColonist = new WanderingColonist(this.gen.generate());
            wanderingColonist.execute(this.game);
        }
    }
}
