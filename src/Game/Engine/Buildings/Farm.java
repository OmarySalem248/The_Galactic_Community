package Game.Engine.Buildings;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Colonist.Profession.Farmer;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Inventory.Items.Seed.Seed;

import java.util.ArrayList;
import java.util.List;

public class Farm extends Building {

    private final ArrayList<PlantIncubater> incubaters      = new ArrayList<>(200);
    private final ArrayList<PlantIncubater> freeIncubaters  = new ArrayList<>(200);
    private final ArrayList<PlantIncubater> activeIncubaters = new ArrayList<>();

    public Farm() {
        super("Farm", 5, 2, 2, Farmer.class, 500, BuildingType.WORKPLACE);
        for (int i = 0; i < 200; i++) {
            PlantIncubater inc = new PlantIncubater();
            incubaters.add(inc);
            freeIncubaters.add(inc);
        }
    }

    public ArrayList<PlantIncubater> getActiveInc() { return activeIncubaters; }
    public ArrayList<PlantIncubater> getAllInc()     { return incubaters; }
    public ArrayList<PlantIncubater> getFreeInc()   { return freeIncubaters; }

    public boolean hasAvailableIncubator() { return !freeIncubaters.isEmpty(); }

    public void plant(Seed seed, GameEventBus bus, Colonist farmer) {
        if (!freeIncubaters.isEmpty()) {
            PlantIncubater inc = freeIncubaters.get(0);
            inc.plantSeed(seed, bus, farmer);
            freeIncubaters.remove(inc);
            activeIncubaters.add(inc);
        }
    }

    public void clearInc(PlantIncubater inc) {
        activeIncubaters.remove(inc);
        freeIncubaters.add(inc);
        inc.clear();
    }

    /** Called when a farmer is unassigned — redistributes their incubators to remaining farmers. */
    public void unassignFarmer(Colonist farmer) {
        // Collect remaining assigned farmers
        List<Colonist> remaining = new ArrayList<>();
        for (Colonist c : getColonists()) {
            if (c != null && c!= farmer) {
                remaining.add(c);
            }
        }

        // Redistribute orphaned incubators round-robin, or leave unassigned if none remain
        int i = 0;
        for (PlantIncubater inc : activeIncubaters) {
            if (inc.isAssignedTo(farmer)) {
                inc.assign(remaining.isEmpty() ? null : remaining.get(i++ % remaining.size()));
            }
        }
    }
}