package Game.Engine.Colonist.Career.Job;

import Game.Engine.Buildings.PlantIncubater;

import java.util.ArrayList;

public class FarmJob extends Job{
    ArrayList<PlantIncubater> incs = new ArrayList<>();
    //Keeps track of incubators farmer is assigned to, can make to-do memory obsolete i really made alot of over convoluted hard coded nonsense to avoid doing this

    public void assignInc(PlantIncubater inc){
        incs.add(inc);
    }
    public void unassignInc(PlantIncubater inc){
        incs.remove(inc);
    }

    public boolean anyIncs(){
        return !incs.isEmpty();
    }


    public boolean anyFreeIncs(){
        for(PlantIncubater inc: incs){
            if (inc.isEmpty()){
                return true;
            }
        }
        return false;
    }

    public PlantIncubater getFreeInc() {
        for(PlantIncubater inc: incs){
            if (inc.isEmpty()){
                return inc;
            }
        }
        return null;
    }
}
