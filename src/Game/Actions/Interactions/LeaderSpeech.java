package Game.Actions.Interactions;

import Game.Colonist.Colonist;
import Game.Colonist.Personality.PersonalityTraits;
import Game.Relationships.Relationship;
import Game.Relationships.RelationshipType;

public class LeaderSpeech extends InteractAction{
    public LeaderSpeech(){}


    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        int effectiveness = (c1.getPersonality().getTrait(PersonalityTraits.INFLUENCE) -c2.getPersonality().getTrait(PersonalityTraits.INFLUENCE)  + rel2.getValue(RelationshipType.PLATONIC))/10;
        rel2.adjustValue(RelationshipType.ADMIRATION,effectiveness);
        c1.modFavourability(effectiveness);
    }
}
