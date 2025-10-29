package Game.Engine.Colonist.Profession;


import Game.Engine.Resources;
import Game.Engine.Colonist.Colonist;


public interface Profession {
    Resources work(Colonist colonist, int usedEnergy);
    String getName();

}



