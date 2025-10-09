package com.GalacticCommunity.service;



import org.springframework.stereotype.Service;
import com.GalacticCommunity.model.Engine.Colony;
import com.GalacticCommunity.model.Engine.Game;
import com.GalacticCommunity.model.Engine.Colonist.Colonist;

@Service
public class GameService {

    private Game game;

    public GameService() {
        this.game = new Game(); // initialize your game
    }

    public Game getGame() {
        return game;
    }

    public void nextTurn() {
        game.nextTurn();
    }

    public Colony getColony() {
        return game.getColony();
    }

    public void feedColonist(Colonist c, int amount) {
        if (c != null) {
            game.getColony().feedColonist(c, amount);
        }
    }


}
