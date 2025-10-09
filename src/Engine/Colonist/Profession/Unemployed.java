package Engine.Colonist.Profession;

import Engine.Colonist.Colonist;
import Engine.Resources;

public class Unemployed implements Profession{
    public Resources work(Colonist colonist, int usedEnergy) {
        return new Resources(0, 0, 0);
    }
    public String getName() { return "Unemployed"; }
}
