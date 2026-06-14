package Game.Engine.Colonist.Profession;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfessionRegistry {
    private static final Map<String, Profession> professions = new HashMap<>();

    static {
        register(new Unemployed());
        register(new Farmer());
        register(new Miner());
        register(new Woodcutter());
        register(new Builder());
        register(new TribeLeader());
    }

    private static void register(Profession prof) {
        professions.put(prof.getName(), prof);
    }

    public static Profession get(String name) {
        return professions.get(name);
    }

    public static Set<String> getAllNames() {
        return professions.keySet();
    }

    public static Collection<Profession> getAllProfessions() {
        return professions.values();
    }
}

