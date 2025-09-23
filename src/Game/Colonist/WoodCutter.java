package Game.Colonist;

import Game.Resources;

public class WoodCutter extends Colonist{
    public WoodCutter(String name) {
        super(name,"woodcutter",1,1);
    }

    @Override
    public Resources work() {
        int woodProduced = getProductivity();
        return new Resources(0, woodProduced,0);
    }

    @Override
    public String getOccupation() {
        return "WoodCutter";
    }
}
