package Game.Engine.Event;

import Game.Engine.Actions.Interactions.MarryAction;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.ColonistGen;
import Game.Engine.Game;

import java.util.Random;

public class EventManager {
    private Game game;
    private ColonistGen gen;

    private Random rand = new Random();
    public EventManager(Game game) {
        this.game = game;
        this.gen = new ColonistGen(game.getColony());


    }

    public void process(){

        if(this.game.getTurn() > 20 && this.game.getTurn() < 40 && this.rand.nextInt(6)>4){
            WanderingColonist wanderingColonist = new WanderingColonist(this.gen.generate());
            wanderingColonist.execute(this.game.getColony());
        }



    }

}
