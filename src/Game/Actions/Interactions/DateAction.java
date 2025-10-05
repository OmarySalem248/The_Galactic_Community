package Game.Actions.Interactions;

import Game.Colonist.Colonist;
import Game.Relationships.Relationship;
import Game.Relationships.RelationshipType;

import java.util.Random;

public class DateAction extends InteractAction {
    private static final double SUCCESS_CHANCE = 0.75; // 75% base chance
    private static final int SUCCESS_BONUS = 10;       // Romantic relationship boost
    private static final int FAILURE_PENALTY = -5;     // Penalty if date fails

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

            System.out.println(c1.getName() + " and " + c2.getName() + " had a lovely date! ❤️ (+"
                    + bonus + " romantic points)");
        } else {
            int penalty = FAILURE_PENALTY - rand.nextInt(5);
            rel1.adjustValue(RelationshipType.ROMANTIC, penalty);
            rel2.adjustValue(RelationshipType.ROMANTIC, penalty);

            System.out.println(c1.getName() + " and " + c2.getName() + " had an awkward date... 💔 ("
                    + penalty + " romantic points)");
        }


        if (rel1.getValue(RelationshipType.SEXUAL) > 70) {
            if (rand.nextDouble() < 0.15) {
                IntercourseAction intercourse = new IntercourseAction();
                intercourse.execute(c1, c2, rel1, rel2);
            }
        }
    }
}
