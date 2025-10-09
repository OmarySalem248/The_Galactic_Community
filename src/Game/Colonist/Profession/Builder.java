package Game.Colonist.Profession;

import Game.Resources;
import Game.Colonist.Colonist;

public class Builder implements Profession {

    public Builder() {

    }

    @Override
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0, 0);
    }

    @Override
    public String getName() {
        return "Builder";
    }
}
