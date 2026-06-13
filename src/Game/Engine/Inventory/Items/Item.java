package Game.Engine.Inventory.Items;


public abstract class Item {
    private final String name;
    private final float weight;

    public Item(String name, float weight) {
        this.name   = name;
        this.weight = weight;
    }

    public String getName()  { return name; }
    public float getWeight() { return weight; }

    @Override
    public String toString() { return name; }
}