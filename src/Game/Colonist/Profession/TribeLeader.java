package Game.Colonist.Profession;

import Game.Colonist.Colonist;
import Game.Resources;

public class TribeLeader implements Profession{
    @Override
    public Resources work(Colonist colonist, int usedEnergy) {
        return null;
    }

    @Override
    public String getName() {
        return "Tribe Leader";
    }
}
