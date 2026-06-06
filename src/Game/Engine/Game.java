package Game.Engine;

import Game.Engine.Buildings.House;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Tile;
import Game.Engine.Time.Event.EventManager;
import Game.Engine.Map.Map;
import Game.Engine.Time.GameClock;

public class Game {

    private int turn;

    private Map map;
    private Colony colony;


    private GameClock clock;
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
            ColonistAvatar avatar = new ColonistAvatar(c,map.getTile(x,y));
            this.map.placeBuilding(house,x,y);
            map.addAvatar(avatar);
            x++;
            y++;
        }
        this.status = this.colony.getStatus();

        this.clock = new GameClock(this);

    }

    public int getTurn() {
        return turn;
    }

    public GameClock getClock() {
        return clock;
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

    public void addColonist(Colonist c, Tile starting){
        colony.addColonist(c);
        map.addAvatar(new ColonistAvatar(c,starting));
    }





}
