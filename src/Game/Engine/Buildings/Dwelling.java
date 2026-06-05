package Game.Engine.Buildings;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.Profession.Profession;

import java.util.ArrayList;

public abstract class Dwelling extends Building{
    private int resLimit;

    private ArrayList<Colonist> residents;


    public Dwelling(String name, int woodCost, int stoneCost, int limit, Class<? extends Profession> compatible,int resLimit) {
        super(name, woodCost, stoneCost, limit, null);
        this.resLimit = resLimit;
        this.residents = new ArrayList<>();
    }
    public void addResident(Colonist resident){
        if((residents.size()+1)<=resLimit){
            resident.setDwelling(this);
            residents.add(resident);
        }

    }
}
