package Game.Engine.Buildings;

import Game.Engine.Colonist.Profession.Profession;

public class Park extends  Building{
    public Park(String name, int woodCost, int stoneCost, int limit) {
        super("Park", 10, 10, Integer.MAX_VALUE, null);
    }
}
