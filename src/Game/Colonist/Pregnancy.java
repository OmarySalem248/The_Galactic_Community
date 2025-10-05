package Game.Colonist;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Pregnancy {
    private Colonist mother;
    private Colonist father;
    private int time;
    private static final Random rand = new Random();

    public Pregnancy(Colonist mother, Colonist father){
        this.mother = mother;
        this.father = father;
        this.time = 0;
        this.mother.setStatus("Got a bun in the oven ;)");
    }

    private static final List<String> MALE_NAMES = Arrays.asList(
            "Liam", "Noah", "Oliver", "Ethan", "Lucas", "Henry","Mark","Amir","Omar"
    );
    private static final List<String> FEMALE_NAMES = Arrays.asList(
            "Emma", "Olivia", "Sophia", "Ava", "Mia", "Luna","Eve","Amanda"
    );

    public Pregnancy progress(){
        this.time++;
        if(time >= 9){
            return  this;
        }
        return null;
    }

    public void birth(){
        Random rand = new Random();

        boolean isMale = rand.nextBoolean();

        String name;
        char sex;
        if (isMale) {
            name = MALE_NAMES.get(rand.nextInt(MALE_NAMES.size()));
            sex= 'M';
        } else {
            name = FEMALE_NAMES.get(rand.nextInt(FEMALE_NAMES.size()));
            sex='F';
        }
        Colonist baby = new Colonist(this.father.getColony(),name,sex);
        baby.setParents(mother, father);
        this.father.getColony().addColonist(baby);
        this.mother.setPregnancy(null);
        this.mother.setStatus("My baby is born :)))))))))");
        this.mother.getColony().setStatus("The colony welcomes "+baby.getName());
        this.mother.postpartumTimer = 3;
    }
}
