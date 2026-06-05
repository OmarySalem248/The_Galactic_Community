package Game.Engine;

import Game.Engine.Buildings.House;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.Profession.ProfessionRegistry;
import Game.Engine.Event.EventManager;
import Game.Engine.Map.Map;

public class Game {

    private int turn;

    private Map map;
    private Colony colony;
    private EventManager eman;
    private String status;

    public Game() {
        this.turn = 1;
        this.map = Map.getBasicMap();
        this.colony = new Colony( new Resources(20, 15, 10),map);
        int x = 10;
        int y= 12;
        for(Colonist c: this.colony.getColonists()){
            House house = new House();
            house.addResident(c);
            this.map.placeBuilding(house,x,y);
            x++;
            y++;
        }
        this.status = this.colony.getStatus();
        this.eman = new EventManager(this);

    }

    public int getTurn() {
        return turn;
    }

    public Map getMap() {
        return map;
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
        this.eman.process();
    }


    public String getFood() { return String.valueOf(getColony().getResources().getFood());
    }

    public String getWood() { return  String.valueOf(getColony().getResources().getWood());
    }

    public String getStone() { return String.valueOf(getColony().getResources().getStone());
    }
}
