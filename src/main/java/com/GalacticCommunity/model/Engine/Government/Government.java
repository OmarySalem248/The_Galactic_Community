package com.GalacticCommunity.model.Engine.Government;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Leader;
import com.GalacticCommunity.model.Engine.Colony;

public abstract class Government {
    protected Colony colony;
    protected Colonist leader;
    protected Leader leadership;


    public Government(Colony colony, Colonist leader) {
        this.colony = colony;
        this.leader = leader;

    }


    public abstract GovernmentType getType();

    public abstract boolean setLeader(Colony colony);

    public abstract void assignJobs();

    public Colonist getLeader() {
        return leader;
    }
}
