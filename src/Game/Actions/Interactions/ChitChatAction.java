package Game.Actions.Interactions;

import Game.Colonist.Colonist;
import Game.Colonist.Personality.PersonalityTraits;
import Game.Relationships.Relationship;
import Game.Relationships.RelationshipType;

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
