package Game.Colonist;

import Game.Resources;

public class WoodCutter extends Colonist{
    public WoodCutter(String name,int age) {
        super(name,"woodcutter",age,1,1);
    }

    @Override
    public Resources work(int usedenergy) {
        int woodProduced = getProductivity(usedenergy);
        return new Resources(0, woodProduced,0);
    }

    @Override
    public String getOccupation() {
        return "WoodCutter";
    }
}
