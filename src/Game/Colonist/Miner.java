package Game.Colonist;
import Game.Resources;
public class Miner extends Colonist {
    public Miner(String name) {
        super(name,"miner",1,1);
    }

    @Override
    public Resources work() {
        int stoneProduced = getProductivity();
        return new Resources(0, 0, stoneProduced);
    }

    @Override
    public String getOccupation() {
        return "Miner";
    }
}