package Game.Engine.Colonist.Profession;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Resources;

public class Miner implements Profession {
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0,usedEnergy );
    }
    public String getName() { return "Miner"; }
}
