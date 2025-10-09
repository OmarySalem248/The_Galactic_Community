package com.GalacticCommunity.model.Engine.Actions.Interactions;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colonist.Personality.PersonalityTraits;
import com.GalacticCommunity.model.Engine.Relationships.Relationship;
import com.GalacticCommunity.model.Engine.Relationships.RelationshipType;

public class ChitChatAction extends InteractAction{
    public ChitChatAction(){

    }

    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        int friendliness = c1.getPersonality().getTrait(PersonalityTraits.FRIENDLINESS);
        int value = friendliness/Math.abs(friendliness);
        rel1.adjustValue(RelationshipType.PLATONIC,value);
        rel2.adjustValue(RelationshipType.PLATONIC,value);
    }

}
