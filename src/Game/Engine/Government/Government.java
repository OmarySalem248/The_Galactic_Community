package Game.Engine.Government;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.Profession.Leader;
import Game.Engine.Colony;

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
