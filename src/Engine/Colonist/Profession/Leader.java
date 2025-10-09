package Engine.Colonist.Profession;

import Engine.Actions.Interactions.LeaderSpeech;
import Engine.Colonist.Colonist;

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
