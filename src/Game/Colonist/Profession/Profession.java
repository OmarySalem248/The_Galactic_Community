package Game.Colonist.Profession;

import Game.Colonist.*;
import Game.Resources;


public interface Profession {
    Resources work(Colonist colonist, int usedEnergy);
    String getName();

}



