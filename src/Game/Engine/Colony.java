package Game.Engine;

import Game.Engine.Actions.ColonyActions.ColonyAction;
import Game.Engine.Buildings.*;
import Game.Engine.Buildings.Projects.BuildingProject;
import Game.Engine.Colonist.Personality.PersonalityFactory;
import Game.Engine.Colonist.Pregnancy;
import Game.Engine.Government.ColonyLeadership;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Resources;
import Game.Engine.Map.Map;
import Game.Engine.Relationships.Relationship;
import Game.Engine.Colonist.Career.Profession.ProfessionRegistry;

import Game.Engine.Actions.ColonyActions.AssignAction;

import Game.Engine.Colonist.Colonist;


import java.util.ArrayList;
import java.util.List;


public class Colony {
    private List<Colonist> colonists;
    List<BuildingProject> projects;

    private List<Building> buildings;

    private PersonalityFactory persfact;
    private int netfoodprod;

    private int fooddemand;

    private ColonyLeadership leadership;


    private String status;
    private Map map;
    private Resources resources;

    private Inventory inv;

    public Colony( Resources startingResources, Map map) {

        this.map = map;
        this.resources = startingResources;
        this.colonists = new ArrayList<>();
        this.buildings = map.getBuildings();
        this.persfact = new PersonalityFactory();
        Colonist Jeff =new Colonist(this,"Jeff", ProfessionRegistry.get("Farmer"),35,1000,1,'M',persfact.futureDictator());

        colonists.add(Jeff);

        colonists.add(new Colonist(this,"Britta",ProfessionRegistry.get("WoodCutter"),28,1000,1,'F',persfact.comedian()));
        Colonist Troy =  new Colonist(this,"Troy",ProfessionRegistry.get("Farmer"), 19,1000,1,'M',persfact.funGuy());

        colonists.add(Troy);


        colonists.add(new Colonist(this,"Abed",ProfessionRegistry.get("Miner"),20,1000,1,'M',persfact.awkwardDude()));
        colonists.add(new Colonist(this,"Annie",ProfessionRegistry.get("Miner"),19,1000,1,'F',persfact.perfectionist()));
        colonists.add(new Colonist(this,"Shirley",ProfessionRegistry.get("WoodCutter"),43,1000,1,'F',persfact.caring()));
        colonists.add(new Colonist(this,"Pierce",ProfessionRegistry.get("Unemployed"),75,1000,1,'M',persfact.turd()));


        fooddemand = colonists.size();
        this.initializeRelationships();

        this.leadership = new ColonyLeadership(this);
        this.status ="The crew are lost!";
        ArrayList<BuildingProject> projects= new ArrayList<BuildingProject>();


        for (Colonist c : colonists) {
            for(Building b :buildings) {
                if (b.isJobCompatible(c) && b.getColonists().size() < b.getColonlimit()) {
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
    public Colonist findColonistById(int id) {
        return colonists.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public Building findBuildingById(int id) {
        return buildings.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }



    public ColonyLeadership getLeadership(){return  leadership;}





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

    public boolean performAction(ColonyAction action) {
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
