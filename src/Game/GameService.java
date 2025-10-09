package Game;


import Engine.Game;

public class GameService {
    private Game game;

    public GameService() {
        this.game = new Game();
    }

    public GameState getGameState() {
        return GameDTO.fromGame(game);
    }

    public void nextTurn() {
        game.nextTurn();
    }

    public void assignColonistToBuilding(int colonistId, int buildingId) {
        game.getColony().assignColonist(colonistId, buildingId);
    }
}
