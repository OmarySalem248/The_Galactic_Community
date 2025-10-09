package Engine.Government;

import Engine.Colonist.Colonist;
import Engine.Colonist.Profession.Leader;
import Engine.Colony;

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
