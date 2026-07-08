package Game.Engine;

import Game.Engine.Buildings.House;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Event.EventManager;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Inventory.Resources;
import Game.Engine.Map.Tile;
import Game.Engine.Map.GameMap;
import Game.Engine.Time.GameClock;
import Game.Modes.BuildMode;

public class Game {

    private final EventManager eman;
    private int turn;

    private GameMap map;
    private Colony colony;

    private final GameEventBus eventBus = new GameEventBus();

    private GameClock clock;

    private BuildMode buildMode = new BuildMode();
    private String status;

    public Game() {
        this.turn = 1;

        this.map = GameMap.getBasicMap();
        this.colony = new Colony( new Resources(20, 15, 10),map);
        int x = 10;
        int y= 12;
        for(Colonist c: this.colony.getColonists()){

            House house = new House();
            house.addResident(c);
            Tile startingTile = map.getTile(x,y);
            ColonistAvatar avatar = new ColonistAvatar(c,map.getTile(x,y));
            avatar.setActionManager(new ActionManager(avatar, startingTile, eventBus,map));
            this.map.placeBuilding(house,x,y);
            map.addAvatar(avatar);
            x++;
            y++;
        }
        this.eman = new EventManager(this, eventBus);
        this.status = this.colony.getStatus();

        this.clock = new GameClock(this);


    }

    public int getTurn() {
        return turn;
    }

    public GameClock getClock() {
        return clock;
    }

    public GameMap getMap() {
        return map;
    }


    public String getStatus(){
        return this.colony.getStatus();
    }
    public GameEventBus getEventBus() {
        return eventBus;
    }


    public Colony getColony() {
        return colony;
    }

    public void addColonist(Colonist c, Tile starting){
        colony.addColonist(c);
        map.addAvatar(new ColonistAvatar(c,starting));
    }


    public BuildMode getBuildMode() {
        return buildMode;
    }
}
