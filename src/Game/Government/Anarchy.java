package Game.Government;

import Game.Colonist.Colonist;
import Game.Colony;

public class Anarchy extends Government{
    public Anarchy(Colony colony, Colonist leader) {
        super(colony, leader);
    }

    @Override
    public GovernmentType getType() {
        return null;
    }

    @Override
    public void assignJobs() {

    }
}
