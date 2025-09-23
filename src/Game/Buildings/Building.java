package Game.Buildings;
import Game.Colonist.Colonist;

import java.util.ArrayList;

public abstract class Building {
    protected String name;
    protected int woodCost;
    protected int stoneCost;

    protected int colonlimit;

    protected ArrayList<Colonist> colonists;
    protected String compatible;


    public Building(String name, int woodCost, int stoneCost,int limit,String compatible) {
        this.name = name;
        this.woodCost = woodCost;
        this.stoneCost = stoneCost;
        this.colonlimit= limit;
        this.compatible = compatible;
        colonists = new ArrayList<>();
    }
    public int getColonlimit(){
        return colonlimit;
    }
    public String getCompatible(){return compatible;}
    public String getName() {
        return name+" "+ colonists.size();
    }
    public ArrayList<Colonist> getColonists(){
        return colonists;
    }

    public int getWoodCost() {
        return woodCost;
    }

    public int getStoneCost() {
        return stoneCost;
    }



    @Override
    public String toString() {
        return name+colonists.size();
    }
}