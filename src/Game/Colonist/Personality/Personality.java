package Game.Colonist.Personality;

import java.util.EnumMap;
import java.util.Map;

public class Personality {
    private Map<PersonalityTraits, Integer> personality;

    // Default: initialize all traits to 0
    public Personality() {
        personality = new EnumMap<>(PersonalityTraits.class);
        for (PersonalityTraits ptrait : PersonalityTraits.values()) {
            personality.put(ptrait, 0);
        }
    }

    // Overloaded constructor: takes a predefined set of traits
    public Personality(Map<PersonalityTraits, Integer> traits) {
        personality = new EnumMap<>(PersonalityTraits.class);
        for (PersonalityTraits ptrait : PersonalityTraits.values()) {
            personality.put(ptrait, traits.getOrDefault(ptrait, 0));
        }
    }

    public int getTrait(PersonalityTraits trait) {
        return personality.get(trait);
    }

    public void modTrait(PersonalityTraits trait, int change) {
        personality.put(trait, personality.get(trait) + change);
    }

    public Map<PersonalityTraits, Integer> getAllTraits() {
        return personality;
    }
}

