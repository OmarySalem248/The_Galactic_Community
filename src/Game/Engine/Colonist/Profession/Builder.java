package Game.Engine.Colonist.Profession;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Resources;

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
