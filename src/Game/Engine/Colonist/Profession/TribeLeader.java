package Game.Engine.Colonist.Profession;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Relationships.Relationship;
import Game.Engine.Relationships.RelationshipType;
import Game.Engine.Resources;

public class TribeLeader extends Leader{




    @Override
    public Resources work(Colonist colonist, int usedEnergy) {
        colonist.getColony().getLeadership().getGovernment().assignJobs();
        return null;
    }



    @Override
    public String getName() {
        return "Tribe Leader";
    }


}
