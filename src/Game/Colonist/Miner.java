package Game.Colonist;
import Game.Resources;
public class Miner extends Colonist {
    public Miner(String name,int age) {
        super(name,"miner",age,1,1);
    }

    @Override
    public Resources work(int usedenergy) {
        int stoneProduced = getProductivity(usedenergy);
        return new Resources(0, 0, stoneProduced);
    }

    @Override
    public String getOccupation() {
        return "Miner";
    }
}