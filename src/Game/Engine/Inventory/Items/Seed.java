package Game.Engine.Inventory.Items;

public abstract class Seed extends Item{
    private Plant plant;
    public Seed(String name, float weight, Plant plant) {
        super(name, weight, ItemType.SEED);
        this.plant = plant;
    }

    public Plant getPlant(){
        return plant;
    }
}
