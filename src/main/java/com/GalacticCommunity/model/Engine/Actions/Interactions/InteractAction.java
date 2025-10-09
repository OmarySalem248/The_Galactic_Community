package com.GalacticCommunity.model.Engine.Actions.Interactions;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Relationships.Relationship;
import com.GalacticCommunity.model.Engine.Relationships.RelationshipType;

public abstract class InteractAction{
    private Colonist colonist1;
    private Colonist colonist2;
    private RelationshipType type;



    public InteractAction(){

    }

    public abstract void execute(Colonist c1, Colonist c2, Relationship rel1,Relationship rel2);

}
