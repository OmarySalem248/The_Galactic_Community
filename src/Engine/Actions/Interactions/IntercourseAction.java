package Engine.Actions.Interactions;

import Engine.Colonist.Colonist;
import Engine.Relationships.Relationship;
import Engine.Colonist.Pregnancy;

import java.util.Random;

public class IntercourseAction extends InteractAction{
    private static final double PREGNANCY_CHANCE = 0.1;
    public IntercourseAction(){

    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        Colonist mother = null;
        Colonist father = null;

        if (c1.getSex() == 'F' && c2.getSex() == 'M') {
            mother = c1;
            father = c2;
        } else if (c2.getSex() == 'F' && c1.getSex() == 'M') {
            mother = c2;
            father = c1;
        }


        Random rand = new Random();
        if (mother != null && father != null && mother.getPregnancy() == null) {
            if (rand.nextDouble() < PREGNANCY_CHANCE && mother.getPostpartumTimer() == 0) {
                Pregnancy pregnancy = new Pregnancy(mother, father);


                if (mother.getColony() != null) {
                    mother.setPregnancy(pregnancy);
                }


            }
        }
    }
}

