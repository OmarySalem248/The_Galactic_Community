package Engine.Government;

import Engine.Colonist.Colonist;
import Engine.Colony;

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
