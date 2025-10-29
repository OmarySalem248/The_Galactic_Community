package Game.Engine.Buildings;
import Game.Engine.Colonist.Profession.Profession;
import Game.Engine.Colonist.Colonist;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"colonists"})
public abstract class Building {
    protected String name;
    protected int woodCost;
    protected int stoneCost;
    private int id;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
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
        this.id = ID_GENERATOR.incrementAndGet();
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

    public int getId() { return  id;
    }


    public String getType() {
        return name;
    }
}