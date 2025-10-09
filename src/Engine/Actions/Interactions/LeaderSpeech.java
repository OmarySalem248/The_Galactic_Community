package Engine.Actions.Interactions;

import Engine.Colonist.Colonist;
import Engine.Colonist.Personality.PersonalityTraits;
import Engine.Relationships.Relationship;
import Engine.Relationships.RelationshipType;

public class LeaderSpeech extends InteractAction{
    public LeaderSpeech(){}


    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        int effectiveness = (c1.getPersonality().getTrait(PersonalityTraits.INFLUENCE) -c2.getPersonality().getTrait(PersonalityTraits.INFLUENCE)  + rel2.getValue(RelationshipType.PLATONIC))/10;
        rel2.adjustValue(RelationshipType.ADMIRATION,effectiveness);
        c1.modFavourability(effectiveness);
    }
}
