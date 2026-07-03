package Game.Engine.Colonist.Career.Profession;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Resources;
import Game.Engine.Time.GameTime;

public class Unemployed extends Profession{
    public Unemployed() {
        super("Unemployed", null,null);
    }

    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0, 0);
    }
    public String getName() { return "Unemployed"; }
    @Override
    public boolean isItWorkHours(GameTime time){
        return false;
    }
}
