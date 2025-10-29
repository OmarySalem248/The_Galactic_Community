package Game.Engine.Actions.Interactions;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Relationships.Relationship;
import Game.Engine.Relationships.RelationshipType;

import java.util.Random;

public class MarryAction extends InteractAction {

    private static final int REQUIRED_ROMANCE = 80;
    private static final double ACCEPT_CHANCE = 0.99;

    private final Random rand = new Random();

    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {


        int love1 = rel1.getValue(RelationshipType.ROMANTIC);
        int love2 = rel2.getValue(RelationshipType.ROMANTIC);


        if (love1 < REQUIRED_ROMANCE || love2 < REQUIRED_ROMANCE) {
            System.out.println(c1.getName() + " and " + c2.getName() + " aren’t ready for marriage yet.");
            return;
        }

        boolean success = rand.nextDouble() < ACCEPT_CHANCE;
        if (success) {
            rel1.setMarriagestatus(true);
            rel2.setMarriagestatus(true);
            rel1.setTypeAdv(RelationshipType.ROMANTIC,"Spouse");
            rel2.setTypeAdv(RelationshipType.ROMANTIC,"Spouse");

            c1.setTaken(true);
            c2.setTaken(true);


        } else {

            rel1.adjustValue(RelationshipType.ROMANTIC, -10);
            rel2.adjustValue(RelationshipType.ROMANTIC, -5);

            System.out.println(c2.getName() + " rejected " + c1.getName() + "’s marriage proposal.");
        }
    }
}