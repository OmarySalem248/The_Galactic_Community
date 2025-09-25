package Game;

public class Game {
    private int turn;
    private Colony colony;

    private String status;

    public Game() {
        this.turn = 1;
        this.colony = new Colony(7, new Resources(20, 15, 10));
        this.status = "The crew are lost!";
    }

    public int getTurn() {
        return turn;
    }

    public String getStatus(){
        return this.status;
    }

    public Colony getColony() {
        return colony;
    }

    public void nextTurn() {
        turn++;
        colony.consumeAndProduce();
        colony.ageColonists();
    }
}
