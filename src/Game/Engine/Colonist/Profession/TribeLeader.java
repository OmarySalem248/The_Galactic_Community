package Game.Engine.Colonist.Profession;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Resources;

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
