package Engine.Colonist.Profession;

import Engine.Resources;
import Engine.Colonist.Colonist;

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
