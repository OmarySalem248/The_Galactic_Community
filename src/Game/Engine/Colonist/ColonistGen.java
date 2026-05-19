package Game.Engine.Colonist;

import Game.Engine.Colonist.Personality.PersonalityFactory;
import Game.Engine.Colonist.Profession.Profession;
import Game.Engine.Colonist.Profession.Unemployed;
import Game.Engine.Colony;

import java.util.*;

public class ColonistGen{

    private final Colony colony;
    private final PersonalityFactory personalityFactory;
    private final Random random;

    // Customisable pools – override via setters for moddability
    private List<String> maleNames = new ArrayList<>(Arrays.asList(
            "Aldric", "Bram", "Cedric", "Dorian", "Ewan",
            "Finn", "Gareth", "Hadwin", "Ivan", "Jasper","Omar","Mark","Rex","Rudy","Ryan","Nolan","Allen","Amir"
    ));
    private List<String> femaleNames = new ArrayList<>(Arrays.asList(
            "Aela", "Bryn", "Calla", "Dara", "Elara",
            "Faye", "Gwen", "Hilda", "Iris", "Jora","Eve","Amanda","Fatima","Halley","Deborah"
    ));
    private List<Profession> professionPool = new ArrayList<>();


    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 45;
    private static final double GAY_CHANCE      = 0.07;
    private static final double BI_CHANCE        = 0.10;

    public ColonistGen(Colony colony) {
        this.colony = colony;
        this.personalityFactory = new PersonalityFactory();
        this.random = new Random();

    }



    public Colonist generate() {
        char sex        = randomSex();
        String name     = randomName(sex);
        int age         = randomAge(MIN_AGE, MAX_AGE);
        int energy      = calcEnergy(age);
        int productivity = calcProductivity(age);
        Profession job  = new Unemployed();
        Colonist c = new Colonist(
                colony, name, job, age, energy, productivity,
                sex, personalityFactory.randomPersonality()
        );
        c.togglesexuality(randomSexuality());
        c.setStatus("Just arrived.");


        return c;
    }






    private String randomName(char sex) {
        List<String> pool = (sex == 'M') ? maleNames : femaleNames;
        return pool.get(random.nextInt(pool.size()));
    }

    private int randomAge(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    private char randomSex() {
        return random.nextBoolean() ? 'M' : 'F';
    }

    private char randomSexuality() {
        double roll = random.nextDouble();
        if (roll < GAY_CHANCE)              return 'G';
        if (roll < GAY_CHANCE + BI_CHANCE)  return 'B';
        return 'S';
    }

    private Profession randomProfession() {
        return professionPool.get(random.nextInt(professionPool.size()));
    }


    private int calcEnergy(int age) {
        if (age < 25) return 2 + random.nextInt(3);
        if (age < 35) return 1+ random.nextInt(3);
        return 0 + random.nextInt(3);
    }

    private int calcProductivity(int age) {
        if (age < 25) return 2 + random.nextInt(3);
        if (age < 35) return 1+ random.nextInt(3);
        return 0 + random.nextInt(3);
    }


    private void assignPartner(Colonist c1, Colonist c2) {
        c1.setEngagedTo(c2);
        c2.setEngagedTo(c1);
        c1.setTaken(true);
        c2.setTaken(true);
    }



    // ---------------------------------------------------------------
    // Setters for customisation
    // ---------------------------------------------------------------

    public void setMaleNames(List<String> names)       { this.maleNames = names; }
    public void setFemaleNames(List<String> names)     { this.femaleNames = names; }
    public void setProfessionPool(List<Profession> p)  { this.professionPool = p; }
    public void addProfession(Profession p)            { this.professionPool.add(p); }
}