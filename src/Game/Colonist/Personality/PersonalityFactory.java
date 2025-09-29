package Game.Colonist.Personality;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class PersonalityFactory {
    public static Personality funGuy() {
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.FRIENDLINESS, 70);
        traits.put(PersonalityTraits.HUMOUR, 50);
        return new Personality(traits);
    }
    public static Personality player() {
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.ROMANCE, 70);
        traits.put(PersonalityTraits.SEDUCTION, 50);
        return new Personality(traits,false);
    }
    public static Personality futureDictator() {
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.INFLUENCE, 70);
        traits.put(PersonalityTraits.HUMOUR, 50);
        traits.put(PersonalityTraits.FRIENDLINESS, -30);
        return new Personality(traits);
    }
    public static Personality futureHaremOwner(){
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.INFLUENCE, 70);
        traits.put(PersonalityTraits.HUMOUR, -20);
        traits.put(PersonalityTraits.FRIENDLINESS, -30);
        traits.put(PersonalityTraits.ROMANCE, 20);
        traits.put(PersonalityTraits.SEDUCTION, 50);
        return new Personality(traits,false);
    }
    public static Personality comedian(){
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.INFLUENCE, 0);
        traits.put(PersonalityTraits.HUMOUR, 50);
        traits.put(PersonalityTraits.FRIENDLINESS, 20);
        traits.put(PersonalityTraits.ROMANCE, 20);
        traits.put(PersonalityTraits.SEDUCTION, 30);
        return new Personality(traits);
    }
    public static Personality awkwardDude(){
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.INFLUENCE, 20);
        traits.put(PersonalityTraits.HUMOUR, 50);
        traits.put(PersonalityTraits.FRIENDLINESS, 20);
        traits.put(PersonalityTraits.ROMANCE, 10);
        traits.put(PersonalityTraits.SEDUCTION, -10);
        return new Personality(traits);
    }
    public static Personality caring(){
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.INFLUENCE, 50);
        traits.put(PersonalityTraits.HUMOUR, 30);
        traits.put(PersonalityTraits.FRIENDLINESS, 90);
        traits.put(PersonalityTraits.ROMANCE, 50);
        traits.put(PersonalityTraits.SEDUCTION, 20);
        return new Personality(traits);
    }
    public static Personality perfectionist(){
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.INFLUENCE, 40);
        traits.put(PersonalityTraits.HUMOUR, 0);
        traits.put(PersonalityTraits.FRIENDLINESS, -20);
        traits.put(PersonalityTraits.ROMANCE, 30);
        traits.put(PersonalityTraits.SEDUCTION, 20);
        return new Personality(traits);
    }
    public static Personality turd(){
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        traits.put(PersonalityTraits.INFLUENCE, 20);
        traits.put(PersonalityTraits.HUMOUR, 10);
        traits.put(PersonalityTraits.FRIENDLINESS, -50);
        traits.put(PersonalityTraits.ROMANCE, -30);
        traits.put(PersonalityTraits.SEDUCTION, -20);
        return new Personality(traits);
    }
    public static Personality randomPersonality() {
        Random  posorneg = new Random();
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        for (PersonalityTraits t : PersonalityTraits.values()) {
            if(posorneg.nextDouble() > 0.5) {
                traits.put(t, (int) (Math.random() * 100));
            }
            else{
                traits.put(t, (int) (Math.random() * -100));
            }
        }
        return new Personality(traits);
    }
}

