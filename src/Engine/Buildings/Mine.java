package Engine.Buildings;

import Engine.Colonist.Profession.Miner;

public class Mine extends Building{
    public Mine() {
        super("Mine",2, 5,2, Miner.class);
    }
}
