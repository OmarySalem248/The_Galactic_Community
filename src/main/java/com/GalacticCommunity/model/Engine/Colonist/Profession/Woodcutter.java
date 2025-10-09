package com.GalacticCommunity.model.Engine.Colonist.Profession;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Resources;

public class Woodcutter implements Profession{
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, usedEnergy, 0); // produces wood
    }
    public String getName() { return "Woodcutter"; }
}
