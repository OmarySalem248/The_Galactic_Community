package Game.Engine.Inventory.Items.Seed;

import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Inventory.Items.Plant.Plant;

public abstract class Seed extends Item {
    private Plant plant;
    public Seed(String name, float weight, Plant plant) {
        super(name, weight, ItemType.SEED);
        this.plant = plant;
    }

    public Plant getPlant(){
        return plant;
    }
}
