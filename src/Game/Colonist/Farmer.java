package Game.Colonist;
import Game.Resources;
public class Farmer extends Colonist {

    public Farmer(String name) {
        super(name,"farmer",1,1);
    }


    @Override
    public Resources work() {
        int foodProduced = getProductivity();
        return new Resources(foodProduced, 0, 0);
    }

    @Override
    public String getOccupation() {
        return "Farmer";
    }
}
