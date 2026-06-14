package Game.Engine.Inventory.Items;


public abstract class Item {
    private final String name;
    private final float weight;

    private ItemType itemType;

    public Item(String name, float weight,ItemType type) {
        this.name   = name;
        this.weight = weight;
        this.itemType = type;
    }

    public String getName()  { return name; }
    public float getWeight() { return weight; }



    @Override
    public String toString() { return name; }

    public ItemType getType() {
        return itemType;
    }
}