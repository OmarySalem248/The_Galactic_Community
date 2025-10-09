package com.GalacticCommunity.model.Engine.Actions.Interactions;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colonist.Personality.Personality;
import com.GalacticCommunity.model.Engine.Colonist.Personality.PersonalityTraits;
import com.GalacticCommunity.model.Engine.Relationships.Relationship;
import com.GalacticCommunity.model.Engine.Relationships.RelationshipType;

public class CoworkerInteract extends InteractAction{
    public CoworkerInteract(){

    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        Personality pers1 = c1.getPersonality();
        Personality pers2 = c2.getPersonality();
        rel1.adjustValue(RelationshipType.PROXIMITY,2);
        rel2.adjustValue(RelationshipType.PROXIMITY,2);
        int value = (pers2.getTrait(PersonalityTraits.FRIENDLINESS)/10 + pers1.getTrait(PersonalityTraits.FRIENDLINESS)/10)/4;
        rel1.adjustValue(RelationshipType.PLATONIC,value);
        rel2.adjustValue(RelationshipType.PLATONIC,value);
        if(pers1.getTrait(PersonalityTraits.INFLUENCE)>50){
            rel2.adjustValue(RelationshipType.ADMIRATION,2);
            c1.modFavourability(1);
        }
    }
}
