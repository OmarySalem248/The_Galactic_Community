package Game.Colonist;
import Game.Resources;
public class Unemployed extends Colonist {
    public Unemployed(String name, int age) {
        super(name,"unemployed",age,0,0);
    }

    @Override
    public Resources work(int energyused) {
        return new Resources(0, 0, 0); // produces nothing
    }

    @Override
    public String getOccupation() {
        return "Unemployed";
    }
}