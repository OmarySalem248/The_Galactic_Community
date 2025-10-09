package Game.Government;

import Game.Colonist.Colonist;
import Game.Colonist.Profession.TribeLeader;
import Game.Colony;

import java.util.Comparator;
import java.util.Optional;

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
