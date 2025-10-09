package com.GalacticCommunity.model.Engine;

import com.GalacticCommunity.model.Engine.Actions.Action;
import com.GalacticCommunity.model.Engine.Actions.AssignAction;
import com.GalacticCommunity.model.Engine.Buildings.*;
import com.GalacticCommunity.model.Engine.Colonist.*;
import com.GalacticCommunity.model.Engine.Buildings.*;
import com.GalacticCommunity.model.Engine.Colonist.Personality.PersonalityFactory;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Farmer;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Miner;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Unemployed;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Woodcutter;
import com.GalacticCommunity.model.Engine.Government.ColonyLeadership;
import com.GalacticCommunity.model.Engine.Relationships.*;
import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colonist.Pregnancy;
import com.GalacticCommunity.model.Engine.Relationships.Relationship;
import com.GalacticCommunity.model.Engine.Relationships.RelationshipManager;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Colony {
    private List<Colonist> colonists;
    List<BuildingProject> projects;

    private List<Building> buildings;
    private RelationshipManager relman;
    private PersonalityFactory persfact;
    private int netfoodprod;

    private int fooddemand;

    private ColonyLeadership leadership;

    private String status;

    private Resources resources;

    public Colony(int startingPopulation, Resources startingResources) {
        this.resources = startingResources;
        this.colonists = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.persfact = new PersonalityFactory();
        colonists.add(new Colonist(this,"Jeff", new Farmer(),35,1,1,'M',persfact.futureDictator()));
        colonists.add(new Colonist(this,"Britta",new Woodcutter(),28,1,1,'F',persfact.comedian()));
        colonists.add(new Colonist(this,"Troy",new Farmer(), 19,1,1,'M',persfact.funGuy()));
        colonists.add(new Colonist(this,"Abed",new Miner(),20,1,1,'M',persfact.awkwardDude()));
        colonists.add(new Colonist(this,"Annie",new Miner(),19,1,1,'F',persfact.perfectionist()));
        colonists.add(new Colonist(this,"Shirley",new Woodcutter(),43,1,1,'F',persfact.caring()));
        colonists.add(new Colonist(this,"Pierce",new Unemployed(),75,1,1,'M',persfact.turd()));
        fooddemand = colonists.size();
        this.initializeRelationships();
        relman = new RelationshipManager(this);
        this.leadership = new ColonyLeadership(this);
        this.status ="The crew are lost!";
        ArrayList<BuildingProject> projects= new ArrayList<BuildingProject>();

        buildings.add(new Farm());
        buildings.add(new Farm());
        buildings.add(new LumberMill());
        buildings.add(new Mine());

        for (Colonist c : colonists) {
            for(Building b :buildings) {
                if (b.isCompatible(c) && b.getColonists().size() < b.getColonlimit()) {
                    AssignAction assign = new AssignAction(c,b);
                    assign.execute(this);

                }
            }

        }
    }
    public void addColonist(Colonist colonist){
        colonists.add(colonist);
    }

    public List<Colonist> getColonists() {
        return colonists;
    }

    public Resources getResources() {
        return resources;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
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

    public ColonyLeadership getLeadership(){return  leadership;}




    public void consumeAndProduce() {
        netfoodprod = 0;
        List<Colonist> farmColonists = new ArrayList<>();
        List<Colonist> otherColonists = new ArrayList<>();
        List<Colonist> unassignedColonists = new ArrayList<>();

        for (Colonist c : colonists) {
            if (!c.isAlive()) continue;
            if (c.getAssignedBuilding() == null) {
                unassignedColonists.add(c);
            } else if (c.getAssignedBuilding().getName().equalsIgnoreCase("Farm")) {
                farmColonists.add(c);
            } else if(c != getLeadership().getCurrentLeader() ) {
                otherColonists.add(c);
            }
        }

        farmColonists.sort(Comparator.comparingInt(Colonist::getAge));
        otherColonists.sort(Comparator.comparingInt(Colonist::getAge));
        unassignedColonists.sort(Comparator.comparingInt(Colonist::getAge));


        List<Colonist> ordered = new ArrayList<>();
        if(getLeadership().getCurrentLeader() != null){
            ordered.add(getLeadership().getCurrentLeader());
        }
        ordered.addAll(farmColonists);
        ordered.addAll(otherColonists);
        ordered.addAll(unassignedColonists);
        int foodAvailable1 = resources.getFood();

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

            if (c.getProfession().getName().equals("Builder") && !projects.isEmpty()) {
                BuildingProject activeProject = projects.get(0);
                int work = c.getEnergy();
                activeProject.contributeWork(work);

                if (activeProject.isCompleted()) {
                    buildings.add(activeProject.completeProject());
                    projects.remove(activeProject);
                    this.setStatus(activeProject.getName() + " has been completed!");
                }
            }

            resources.setFood(foodAvailable);

            if (c.getAssignedBuilding() != null && consumed > 0 && c.isAlive()) {
                produce(c,consumed);

                foodAvailable = resources.getFood();
            }

            resources.setFood(foodAvailable);

        }
        netfoodprod = foodAvailable -foodAvailable1;

        removeDeadColonists();
    }
    public int getNetfoodprod(){return netfoodprod;}
    public void ageColonists() {
        List<Pregnancy> newBirths = new ArrayList<>();

        for (Colonist c : colonists) {
            c.age();


            if (c.getPregnancy() != null) {
                Pregnancy due = c.getPregnancy().progress();
                if(due != null){
                    newBirths.add(due);
                }
            }
        }


        for(Pregnancy due: newBirths){
            due.birth();
        }
    }

    private void produce(Colonist c,int usedenergy){
        Resources produced = c.work(usedenergy);
        if(produced != null) {
            resources.addFood(produced.getFood());
            resources.addWood(produced.getWood());
            resources.addStone(produced.getStone());
        }
    }

    private void removeDeadColonists() {
        if(getLeadership().getCurrentLeader()!= null){
            if(!getLeadership().getCurrentLeader().isAlive()) {
                getLeadership().changeLeader();
            }
        }
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

    public int getFoodDemand() {
        fooddemand = 0;
        for(Colonist c:colonists){
            fooddemand += c.getFoodConsumption();
        }
        return fooddemand;
    }
}
