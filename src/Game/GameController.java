package Game;

import Game.Actions.AssignAction;
import Game.Buildings.Building;
import Game.Colonist.Colonist;
import Game.Colonist.Profession.ProfessionRegistry;

public class GameController {
    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }


    public void nextTurn() {
        game.nextTurn();
    }


    public boolean feedColonist(Colonist colonist, int food) {
        return game.getColony().feedColonist(colonist, food);
    }


    public boolean assignColonistToBuilding(Colonist colonist, Building building) {
        AssignAction action = new AssignAction(colonist, building);
        return game.getColony().performAction(action);
    }


    public boolean changeProfession(Colonist colonist, String professionName) {
        if (colonist == null) return false;

        colonist.setProfession(ProfessionRegistry.create(professionName));

        // Ensure building compatibility
        if (colonist.getAssignedBuilding() != null && !colonist.getAssignedBuilding().isCompatible(colonist)) {
            colonist.unassignBuilding();
        }

        return true;
    }

    public Game getGame() {
        return game;
    }
}
