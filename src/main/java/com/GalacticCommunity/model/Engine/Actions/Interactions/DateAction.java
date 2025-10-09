package com.GalacticCommunity.model.Engine.Actions.Interactions;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Relationships.Relationship;
import com.GalacticCommunity.model.Engine.Relationships.RelationshipType;

import java.util.Random;

public class DateAction extends InteractAction {
    private static final double SUCCESS_CHANCE = 0.75;
    private static final int SUCCESS_BONUS = 2;
    private static final int FAILURE_PENALTY = -2;

    private Random rand = new Random();

    public DateAction() {
        super();
    }

    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {





        boolean success = rand.nextDouble() < SUCCESS_CHANCE;

        if (success) {
            int bonus = SUCCESS_BONUS + rand.nextInt(5); // add some randomness
            rel1.adjustValue(RelationshipType.ROMANTIC, bonus);
            rel2.adjustValue(RelationshipType.ROMANTIC, bonus);
            rel1.adjustValue(RelationshipType.SEXUAL, bonus/2);
            rel2.adjustValue(RelationshipType.SEXUAL, bonus/2);

            rel1.adjustValue(RelationshipType.PLATONIC, 3);
            rel2.adjustValue(RelationshipType.PLATONIC, 3);


        } else {
            int penalty = FAILURE_PENALTY - rand.nextInt(5);
            rel1.adjustValue(RelationshipType.ROMANTIC, penalty);
            rel2.adjustValue(RelationshipType.ROMANTIC, penalty);


        }


        if (rel1.getValue(RelationshipType.SEXUAL) > 50) {
            if (rand.nextDouble() < 0.05) {
                IntercourseAction intercourse = new IntercourseAction();
                intercourse.execute(c1, c2, rel1, rel2);
            }
        }
    }
}
