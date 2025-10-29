package Game.Engine.Actions.Interactions;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.Personality.PersonalityTraits;
import Game.Engine.Relationships.Relationship;
import Game.Engine.Relationships.RelationshipType;

public class LeaderSpeech extends InteractAction{
    public LeaderSpeech(){}


    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        int effectiveness = (c1.getPersonality().getTrait(PersonalityTraits.INFLUENCE) -c2.getPersonality().getTrait(PersonalityTraits.INFLUENCE)  + rel2.getValue(RelationshipType.PLATONIC))/10;
        rel2.adjustValue(RelationshipType.ADMIRATION,effectiveness);
        c1.modFavourability(effectiveness);
    }
}
