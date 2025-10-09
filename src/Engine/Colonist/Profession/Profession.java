package Engine.Colonist.Profession;

import Engine.Colonist.*;
import Engine.Resources;


public interface Profession {
    Resources work(Colonist colonist, int usedEnergy);
    String getName();

}



