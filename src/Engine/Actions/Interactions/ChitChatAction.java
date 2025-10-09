package Engine.Actions.Interactions;

import Engine.Colonist.Colonist;
import Engine.Colonist.Personality.PersonalityTraits;
import Engine.Relationships.Relationship;
import Engine.Relationships.RelationshipType;

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
