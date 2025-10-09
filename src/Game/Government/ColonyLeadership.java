package Game.Government;

import Game.Buildings.Mine;
import Game.Colonist.Colonist;
import Game.Colonist.Profession.TribeLeader;
import Game.Colony;
import Game.Government.TribalGovernment;

import javax.swing.*;
import java.util.Comparator;
import java.util.Optional;

public  class ColonyLeadership {
    private Colonist currentLeader;
    private GovernmentType governmentType;
    private Government government;

    private Colony colony;
    private boolean leadershipEstablished;

    public ColonyLeadership(Colony colony) {
        this.currentLeader = null;
        this.governmentType = GovernmentType.ANARCHY;
        this.leadershipEstablished = false;
        this.colony = colony;
        this.government = new Anarchy(colony,null);
    }

    public Colonist getCurrentLeader() {
        return currentLeader;
    }

    public GovernmentType getGovernmentType() {
        return governmentType;
    }

    public Government getGovernment(){return government;}

    public void changeLeader(){
        boolean leader = this.government.setLeader(colony);
        if(!leader) {
            this.government = new Anarchy(colony, null);
            leadershipEstablished = false;
            return;
        }
        currentLeader = this.government.getLeader();
    }


    public void setLeadership(Colony colony) {
        if (!leadershipEstablished) {
            Optional<Colonist> best = colony.getColonists().stream()
                    .filter(Colonist::isAlive)
                    .max(Comparator.comparingInt(Colonist::getFavourability));

            if (best.isPresent()) {
                currentLeader = best.get();
                leadershipEstablished = true;
                governmentType = GovernmentType.TRIBAL;
                currentLeader.setProfession(new TribeLeader());
                this.government = new TribalGovernment(this.colony,currentLeader);

            }
        }
    }



}
