package com.GalacticCommunity.model.Engine.Colonist.Profession;

import com.GalacticCommunity.model.Engine.Colonist.*;
import com.GalacticCommunity.model.Engine.Resources;
import com.GalacticCommunity.model.Engine.Colonist.Colonist;


public interface Profession {
    Resources work(Colonist colonist, int usedEnergy);
    String getName();

}



