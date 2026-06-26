package Game.Engine.Inventory.Items.Seed;

import Game.Engine.Inventory.Items.Plant.Plant;
import Game.Engine.Inventory.Items.Plant.UberryPlant;
import Game.Engine.Inventory.Items.Seed.Seed;

public class UberrySeed extends Seed {
    public UberrySeed() {

        super("UtopiBerry Seed", 1);

    }
    @Override
    public Plant createPlant() {
        return new UberryPlant(); // fresh instance every call
    }
}
