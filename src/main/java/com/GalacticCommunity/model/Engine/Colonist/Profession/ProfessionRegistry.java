package com.GalacticCommunity.model.Engine.Colonist.Profession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfessionRegistry {
    private static final Map<String, Class<? extends Profession>> professions = new HashMap<>();

    static {
        register(new Unemployed());
        register(new Farmer());
        register(new Miner());
        register(new Woodcutter());
        register(new Builder());
    }

    private static void register(Profession prof) {
        professions.put(prof.getName(), prof.getClass());
    }

    public static Set<String> getAllNames() {
        return professions.keySet();
    }

    public static Profession create(String name) {
        Class<? extends Profession> clazz = professions.get(name);
        if (clazz == null) return null;
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create profession: " + name, e);
        }
    }
}

