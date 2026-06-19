package Game.Engine.Buildings;

import Game.Engine.Colonist.Profession.Farmer;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.Seed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Farm extends Building {


    private ArrayList<PlantIncubater> incubaters = new ArrayList<PlantIncubater>(100);
    private ArrayList<PlantIncubater> freeincubaters = new ArrayList<PlantIncubater>(100);
    private ArrayList<PlantIncubater> activeincubaters = new ArrayList<PlantIncubater>(100);


    public Farm() {

        super("Farm", 5, 2,2, Farmer.class,500,BuildingType.WORKPLACE);
        for(int i = 1; i <= 100; i++){
            incubaters.add(new PlantIncubater());

        }
        freeincubaters = incubaters;

    }


    public  ArrayList<PlantIncubater> getActiveInc() {
        return activeincubaters;
    }
    public  ArrayList<PlantIncubater> getAllInc() {
        return incubaters;
    }

    public ArrayList<PlantIncubater> getFreeInc() {
        return freeincubaters;
    }

    public void plant(Seed seed, GameEventBus bus) {
        if(freeincubaters.size() > 0){
            PlantIncubater inc = freeincubaters.get(0);
            inc.plantSeed(seed, bus );
            freeincubaters.remove(inc);
            activeincubaters.add(inc);
            System.out.print(seed.getName() + "planted!!!!");
        }
    }
}
