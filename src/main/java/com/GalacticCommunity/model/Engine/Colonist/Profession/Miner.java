package com.GalacticCommunity.model.Engine.Colonist.Profession;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Resources;

public class Miner implements Profession {
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0,usedEnergy );
    }
    public String getName() { return "Miner"; }
}
