package Game;

public class Game {
    private int turn;
    private Colony colony;

    public Game() {
        this.turn = 1;
        this.colony = new Colony(5, new Resources(20, 15, 10));
    }

    public int getTurn() {
        return turn;
    }

    public Colony getColony() {
        return colony;
    }

    public void nextTurn() {
        turn++;
        colony.consumeAndProduce();
    }
}
