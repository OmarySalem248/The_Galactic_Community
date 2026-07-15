package Game.Engine.Colonist.Memory.Search;

/**
 * Abstract base for a resource search query carried by a SearchTile.
 */


import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;

public abstract class Search {
    private final int quantity;

    public Search(int quantity) {
        this.quantity = quantity;
    }

    public abstract float getSuitability(Inventory inv);
    public int getQuantity() { return quantity; }

    public abstract boolean matches(Item item);
    public abstract String describe();
}
