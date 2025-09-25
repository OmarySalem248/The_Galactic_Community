package Game.Colonist;

import Game.Resources;

public class Woodcutter implements Profession{
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, usedEnergy, 0); // produces wood
    }
    public String getName() { return "Woodcutter"; }
}
