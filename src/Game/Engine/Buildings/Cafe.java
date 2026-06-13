package Game.Engine.Buildings;

import Game.Engine.Colonist.Profession.Profession;

public class Cafe extends Building{
    public Cafe(String name, int woodCost, int stoneCost, int limit) {
        super("Cafe", 100, 50, Integer.MAX_VALUE, null);
    }
}
