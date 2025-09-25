package Game.Colonist;

import Game.Colonist.*;
import Game.Resources;
public interface Profession {
    Resources work(Colonist colonist, int usedEnergy);
    String getName();
}



