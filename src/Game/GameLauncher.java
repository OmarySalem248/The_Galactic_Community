package Game;

import Game.Windows.GameWindow;

public class GameLauncher {
    public static void main(String[] args) {
        System.out.println("Launching the colony builder...");
        Game game = new Game();
        GameWindow.startGame(game);
    }
}