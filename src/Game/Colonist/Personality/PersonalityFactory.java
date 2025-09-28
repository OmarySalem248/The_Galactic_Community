package Game.Colonist.Personality;

import java.util.EnumMap;
import java.util.Map;

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
        return new Personality(traits);
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
        return new Personality(traits);
    }

    public static Personality randomPersonality() {
        Map<PersonalityTraits, Integer> traits = new EnumMap<>(PersonalityTraits.class);
        for (PersonalityTraits t : PersonalityTraits.values()) {
            traits.put(t, (int)(Math.random() * 100));
        }
        return new Personality(traits);
    }
}

