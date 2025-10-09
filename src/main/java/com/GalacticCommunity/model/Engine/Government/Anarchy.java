package com.GalacticCommunity.model.Engine.Government;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colony;

public class Anarchy extends Government{
    public Anarchy(Colony colony, Colonist leader) {
        super(colony, leader);
    }

    @Override
    public GovernmentType getType() {
        return null;
    }
    @Override
    public boolean setLeader(Colony colony) {
        return false;
    }

    @Override
    public void assignJobs() {

    }
}
