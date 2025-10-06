package Game.Government;

import Game.Colonist.Colonist;
import Game.Colony;

public abstract class Government {
    protected Colony colony;
    protected Colonist leader;


    public Government(Colony colony, Colonist leader) {
        this.colony = colony;
        this.leader = leader;

    }


    public abstract GovernmentType getType();

    public abstract void assignJobs();
}
