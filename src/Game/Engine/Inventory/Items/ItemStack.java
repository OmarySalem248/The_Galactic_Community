package Game.Engine.Inventory.Items;

public class ItemStack {
    private final Item item;
    private int quantity;

    public ItemStack(Item item, int quantity) {
        this.item     = item;
        this.quantity = quantity;
    }

    public Item getItem()      { return item; }
    public int getQuantity()   { return quantity; }
    public float getTotalWeight() { return item.getWeight() * quantity; }

    public void add(int amount)    { this.quantity += amount; }
    public void remove(int amount) { this.quantity = Math.max(0, this.quantity - amount); }
    public boolean isEmpty()       { return quantity <= 0; }

    @Override
    public String toString() { return item.getName() + " x" + quantity; }
}