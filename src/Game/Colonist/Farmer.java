package Game.Colonist;
import Game.Resources;
public class Farmer extends Colonist {

    public Farmer(String name,int age) {
        super(name,"farmer",age,1,1);
    }


    @Override
    public Resources work(int usedenergy) {
        int foodProduced = getProductivity(usedenergy);
        return new Resources(2*foodProduced, 0, 0);
    }

    @Override
    public String getOccupation() {
        return "Farmer";
    }
}
