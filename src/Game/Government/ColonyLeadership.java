package Game.Government;

import Game.Colonist.Colonist;
import Game.Colonist.Profession.TribeLeader;
import Game.Colony;

import java.util.Comparator;
import java.util.Optional;

public class ColonyLeadership {
    private Colonist currentLeader;
    private GovernmentType governmentType;
    private boolean leadershipEstablished;

    public ColonyLeadership() {
        this.currentLeader = null;
        this.governmentType = GovernmentType.ANARCHY; // default state
        this.leadershipEstablished = false;
    }

    public Colonist getCurrentLeader() {
        return currentLeader;
    }

    public GovernmentType getGovernmentType() {
        return governmentType;
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

            }
        }
    }
}
