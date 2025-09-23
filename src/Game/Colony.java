package Game;

import Game.Actions.Action;
import Game.Buildings.Building;
import Game.Colonist.Colonist;
import Game.Colonist.Farmer;
import Game.Colonist.Miner;
import Game.Colonist.WoodCutter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Colony {
    private List<Colonist> colonists;

    private List<Building> buildings;
    private Resources resources;

    public Colony(int startingPopulation, Resources startingResources) {
        this.resources = startingResources;
        this.colonists = new ArrayList<>();
        this.buildings = new ArrayList<>();
        colonists.add(new Farmer("Jeff"));
        colonists.add(new WoodCutter("Britta"));
        colonists.add(new Farmer("Troy"));
        colonists.add(new Miner("Abed"));
        colonists.add(new Miner("Annie"));
        colonists.add(new WoodCutter("Shirley"));
        colonists.add(new Farmer("Pierce"));
    }

    public List<Colonist> getColonists() {
        return colonists;
    }

    public Resources getResources() {
        return resources;
    }

    public int getPopulation() {
        return colonists.size();
    }

    public void consumeAndProduce() {
        // Consume food
        int foodNeeded = colonists.size();
        resources.addFood(-foodNeeded);

        // Starvation handling
        if (resources.getFood() < 0) {
            int deficit = -resources.getFood();
            Iterator<Colonist> it = colonists.iterator();
            while (deficit > 0 && it.hasNext()) {
                it.next();
                it.remove();
                deficit--;
            }
            resources.setFood(0);
        }

        for(Building b: buildings) {
            for (Colonist c : b.getColonists()) {
                Resources produced = c.work();
                resources.addFood(produced.getFood());
                resources.addWood(produced.getWood());
                resources.addStone(produced.getStone());
            }
        }
    }
    public void addBuilding(Building building){
        this.buildings.add(building);
    }

    public boolean performAction(Action action) {
        return action.execute(this);
    }

    public boolean feedColonist(Colonist c, int extraFood) {
        if (resources.getFood() >= extraFood) {
            resources.addFood(-extraFood); // consume food from colony
            c.feedExtra(extraFood);        // increase colonist energy
            return true;
        }
        return false; // not enough food
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}