package Game.Engine.Actions.Interactions;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.Personality.PersonalityTraits;
import Game.Engine.Relationships.RelationshipType;

public class ChitChatAction extends InteractAction {

    public ChitChatAction(Colonist c1, Colonist c2) {
        super(c1, c2, calcDuration(c1, c2));
    }

    /** Duration based on sociability of both colonists — more sociable = longer chat. */
    private static int calcDuration(Colonist c1, Colonist c2) {
        int soc1 = c1.getPersonality().getTrait(PersonalityTraits.FRIENDLINESS);
        int soc2 = c2.getPersonality().getTrait(PersonalityTraits.FRIENDLINESS);
        // Base 5 ticks, up to 20 based on combined sociability (0-100 each)
        return 5 + (soc1 + soc2) / 10;
    }

    @Override
    protected void onTick() {
        // Small platonic gain each tick
        rel1.adjustValue(RelationshipType.PLATONIC, 1);
        rel2.adjustValue(RelationshipType.PLATONIC, 1);
    }

    @Override
    protected void onComplete() {
        c1.setStatus("Had a nice chat with " + c2.getName());
        c2.setStatus("Had a nice chat with " + c1.getName());
    }
}