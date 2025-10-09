package com.GalacticCommunity.model.Engine.Buildings;
import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Profession;

import java.util.ArrayList;

public abstract class Building {
    protected String name;
    protected int woodCost;
    protected int stoneCost;
    private Class<? extends Profession> compatibleProfession;

    protected int colonlimit;

    protected ArrayList<Colonist> colonists;
    protected Profession compatible;


    public Building(String name, int woodCost, int stoneCost, int limit,Class<? extends Profession> compatible) {
        this.name = name;
        this.woodCost = woodCost;
        this.stoneCost = stoneCost;
        this.colonlimit= limit;
        this.compatibleProfession = compatible;
        colonists = new ArrayList<>();
    }
    public Class<? extends Profession> getCompatible() {
        return compatibleProfession;
    }
    public int getColonlimit(){
        return colonlimit;
    }

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


    public boolean isCompatible(Colonist selected) {
        return getCompatible().isInstance(selected.getProfession());
    }

    public boolean hasVacancy() {
        return colonists.size() < colonlimit;
    }
}