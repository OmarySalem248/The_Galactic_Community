package Game.Actions.Interactions;

import Game.Colonist.Colonist;
import Game.Colonist.Personality.Personality;
import Game.Colonist.Personality.PersonalityTraits;
import Game.Relationships.Relationship;
import Game.Relationships.RelationshipType;

public class CoworkerInteract extends InteractAction{
    public CoworkerInteract(){

    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        Personality pers1 = c1.getPersonality();
        Personality pers2 = c2.getPersonality();
        rel1.adjustValue(RelationshipType.PLATONIC,pers2.getTrait(PersonalityTraits.FRIENDLINESS)/10);
        rel2.adjustValue(RelationshipType.PLATONIC,pers1.getTrait(PersonalityTraits.FRIENDLINESS)/10);
        if(pers1.getTrait(PersonalityTraits.INFLUENCE)>50){
            rel2.adjustValue(RelationshipType.ADMIRATION,1);
            c1.modFavourability(5);
        }
    }
}
