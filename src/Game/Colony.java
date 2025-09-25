package Game;

import Game.Actions.Action;
import Game.Buildings.Building;
import Game.Colonist.Colonist;
import Game.Colonist.Farmer;
import Game.Colonist.Miner;
import Game.Colonist.WoodCutter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class Colony {
    private List<Colonist> colonists;

    private List<Building> buildings;

    private  List<Colonist> unassignedColonists;
    private Resources resources;

    public Colony(int startingPopulation, Resources startingResources) {
        this.resources = startingResources;
        this.colonists = new ArrayList<>();
        this.buildings = new ArrayList<>();
        colonists.add(new Farmer("Jeff",35));
        colonists.add(new WoodCutter("Britta",28));
        colonists.add(new Farmer("Troy", 19));
        colonists.add(new Miner("Abed",20));
        colonists.add(new Miner("Annie",19));
        colonists.add(new WoodCutter("Shirley",43));
        colonists.add(new Farmer("Pierce",75));
        this.unassignedColonists = colonists;
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

    public int getFoodNeeded(){
        int demand = 0;
        for(Colonist c : colonists){
            demand += c.getFoodConsumption();
        }
        return  demand;
    }

    public void consumeAndProduce() {
        List<Colonist> farmColonists = new ArrayList<>();
        List<Colonist> otherColonists = new ArrayList<>();
        List<Colonist> unassignedColonists = new ArrayList<>();

        for (Colonist c : colonists) {
            if (!c.isAlive()) continue;
            if (c.getAssignedBuilding() == null) {
                unassignedColonists.add(c);
            } else if (c.getAssignedBuilding().getName().equalsIgnoreCase("Farm")) {
                farmColonists.add(c);
            } else {
                otherColonists.add(c);
            }
        }

        farmColonists.sort(Comparator.comparingInt(Colonist::getAge));
        otherColonists.sort(Comparator.comparingInt(Colonist::getAge));
        unassignedColonists.sort(Comparator.comparingInt(Colonist::getAge));

        // Process order: farms first → others → unassigned
        List<Colonist> ordered = new ArrayList<>();
        ordered.addAll(farmColonists);
        ordered.addAll(otherColonists);
        ordered.addAll(unassignedColonists);

        int foodAvailable = resources.getFood();

        for (Colonist c : ordered) {
            int consumed = 0;

            if (foodAvailable >= c.getFoodConsumption()) {
                consumed = c.getFoodConsumption();
                foodAvailable -= consumed;
            } else if (foodAvailable > 0) {
                consumed = foodAvailable;
                foodAvailable = 0;
            } else {
                c.takeDamage(10);
            }
            resources.setFood(foodAvailable);

            if (c.getAssignedBuilding() != null && consumed > 0 && c.isAlive()) {
                produce(c,consumed);

                foodAvailable = resources.getFood();
            }

            resources.setFood(foodAvailable);
        }

        removeDeadColonists();
    }
    private void produce(Colonist c,int usedenergy){
        Resources produced = c.work(usedenergy);
        resources.addFood(produced.getFood());
        resources.addWood(produced.getWood());
        resources.addStone(produced.getStone());
    }

    private void removeDeadColonists() {
        colonists.removeIf(c -> !c.isAlive());
    }







    public void addBuilding(Building building){
        this.buildings.add(building);
    }

    public boolean performAction(Action action) {
        return action.execute(this);
    }

    public boolean feedColonist(Colonist c, int extraFood) {
        if (resources.getFood() >= extraFood) {
            c.feedExtra(extraFood);
            return true;
        }
        return false;
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}
