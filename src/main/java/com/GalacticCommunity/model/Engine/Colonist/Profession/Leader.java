package com.GalacticCommunity.model.Engine.Colonist.Profession;

import com.GalacticCommunity.model.Engine.Actions.Interactions.LeaderSpeech;
import com.GalacticCommunity.model.Engine.Colonist.Colonist;

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
