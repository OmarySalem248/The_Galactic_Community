package com.GalacticCommunity.model.Engine.Colonist.Profession;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Resources;

public class Farmer implements Profession {
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources((int) Math.round(usedEnergy *1.5), 0, 0);
    }
    public String getName() { return "Farmer"; }
}