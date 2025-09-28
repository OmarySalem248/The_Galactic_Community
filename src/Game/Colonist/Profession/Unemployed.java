package Game.Colonist.Profession;

import Game.Colonist.Colonist;
import Game.Resources;

public class Unemployed implements Profession{
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0, 0);
    }
    public String getName() { return "Unemployed"; }
}
