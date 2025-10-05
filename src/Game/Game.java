package Game;

public class Game {
    private int turn;
    private Colony colony;

    private String status;

    public Game() {
        this.turn = 1;
        this.colony = new Colony(7, new Resources(20, 15, 10));
        this.status = this.colony.getStatus();
    }

    public int getTurn() {
        return turn;
    }

    public String getStatus(){
        return this.colony.getStatus();
    }

    public Colony getColony() {
        return colony;
    }

    public void nextTurn() {
        turn++;
        colony.consumeAndProduce();
        colony.ageColonists();
        colony.developRelationships();
        if (this.turn ==12){
            colony.getLeadership().setLeadership(colony);
        }
    }
}
