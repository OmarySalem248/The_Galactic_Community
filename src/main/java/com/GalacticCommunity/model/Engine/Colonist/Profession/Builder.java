package com.GalacticCommunity.model.Engine.Colonist.Profession;

import com.GalacticCommunity.model.Engine.Resources;
import com.GalacticCommunity.model.Engine.Colonist.Colonist;

public class Builder implements Profession {

    public Builder() {

    }

    @Override
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0, 0);
    }

    @Override
    public String getName() {
        return "Builder";
    }
}
