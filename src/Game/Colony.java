package Game;

import Game.Actions.Action;
import Game.Buildings.Building;
import Game.Colonist.*;
import Game.Colonist.Personality.Personality;
import Game.Colonist.Personality.PersonalityFactory;
import Game.Colonist.Profession.Farmer;
import Game.Colonist.Profession.Miner;
import Game.Colonist.Profession.Unemployed;
import Game.Colonist.Profession.Woodcutter;
import Game.Relationships.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Colony {
    private List<Colonist> colonists;

    private List<Building> buildings;
    private RelationshipManager relman;
    private PersonalityFactory persfact;


    private Resources resources;

    public Colony(int startingPopulation, Resources startingResources) {
        this.resources = startingResources;
        this.colonists = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.persfact = new PersonalityFactory();
        colonists.add(new Colonist("Jeff", new Farmer(),35,1,1,'M',persfact.futureDictator()));
        colonists.add(new Colonist("Britta",new Woodcutter(),28,1,1,'F',persfact.randomPersonality()));
        colonists.add(new Colonist("Troy",new Farmer(), 19,1,1,'M',persfact.funGuy()));
        colonists.add(new Colonist("Abed",new Miner(),20,1,1,'M',persfact.randomPersonality()));
        colonists.add(new Colonist("Annie",new Miner(),19,1,1,'F',persfact.randomPersonality()));
        colonists.add(new Colonist("Shirley",new Woodcutter(),43,1,1,'F',persfact.randomPersonality()));
        colonists.add(new Colonist("Pierce",new Unemployed(),75,1,1,'M',persfact.randomPersonality()));
        this.initializeRelationships();
        relman = new RelationshipManager(this);
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

    public void initializeRelationships() {
        for (Colonist c1 : colonists) {
            for (Colonist c2 : colonists) {
                if (c1 != c2) {
                    if (!c1.getRelationships().hasRelationshipWith(c2.getName())) {
                        c1.getRelationships().addRelationship(
                                new Relationship(c1,c2, "None")
                        );
                    }
                }
            }
        }
    }
    public void developRelationships() {
        relman.developRelationships();
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
    public void ageColonists(){
        for(Colonist c : colonists){
            c.age();
        }
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
