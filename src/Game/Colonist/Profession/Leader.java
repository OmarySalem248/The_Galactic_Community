package Game.Colonist.Profession;

import Game.Actions.Interactions.LeaderSpeech;
import Game.Colonist.Colonist;

public abstract class Leader implements Profession{
    LeaderSpeech speech = new LeaderSpeech();
    Colonist successor;
    public Colonist getSuccessor(){
        return successor;
    }


    public void setSuccessor(Colonist successor){
        this.successor = successor;
    }
}
