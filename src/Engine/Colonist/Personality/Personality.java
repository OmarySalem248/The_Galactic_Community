package Engine.Colonist.Personality;

import java.util.EnumMap;
import java.util.Map;

public class Personality {
    private boolean mono;
    private Map<PersonalityTraits, Integer> personality;

    // Default: initialize all traits to 0
    public Personality() {
        personality = new EnumMap<>(PersonalityTraits.class);
        for (PersonalityTraits ptrait : PersonalityTraits.values()) {
            personality.put(ptrait, 0);
        }
        this.mono =true;
    }

    public Personality(Map<PersonalityTraits, Integer> traits) {
        personality = new EnumMap<>(PersonalityTraits.class);
        for (PersonalityTraits ptrait : PersonalityTraits.values()) {
            personality.put(ptrait, traits.getOrDefault(ptrait, 0));
        }
        this.mono = true;
    }
    public Personality(Map<PersonalityTraits, Integer> traits,boolean mono) {
        personality = new EnumMap<>(PersonalityTraits.class);
        for (PersonalityTraits ptrait : PersonalityTraits.values()) {
            personality.put(ptrait, traits.getOrDefault(ptrait, 0));
        }
        this.mono = mono;
    }

    public int getTrait(PersonalityTraits trait) {
        return personality.get(trait);
    }
    public boolean getMono(){return mono;}

    public void modTrait(PersonalityTraits trait, int change) {
        personality.put(trait, personality.get(trait) + change);
    }

    public Map<PersonalityTraits, Integer> getAllTraits() {
        return personality;
    }
}

