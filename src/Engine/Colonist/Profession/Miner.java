package Engine.Colonist.Profession;

import Engine.Colonist.Colonist;
import Engine.Resources;

public class Miner implements Profession {
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0,usedEnergy );
    }
    public String getName() { return "Miner"; }
}
