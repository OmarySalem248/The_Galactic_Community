package Game.Colonist.Profession;

import Game.Colonist.Colonist;
import Game.Resources;

public class Farmer implements Profession {
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(usedEnergy * 2, 0, 0);
    }
    public String getName() { return "Farmer"; }
}