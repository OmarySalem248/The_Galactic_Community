package Game.Engine.Colonist.Memory.Search;

/**
 * Abstract base for a resource search query carried by a SearchTile.
 */


import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.Tiles.Coords;

public abstract class Search {
    private final int quantity;

    private Coords origin;
    private Coords dest;

    public Search(int quantity, Coords origin, Coords dest) {

        this.quantity = quantity;
        this.dest =dest;
        this.origin = origin;
    }

    public abstract float getSuitability(Inventory inv);
    public int getQuantity() { return quantity; }

    public abstract boolean matches(Item item);
    public abstract String describe();


    public Coords getDest() {
        return dest;
    }

    public boolean invMatches(Inventory inv) {
        return false;
    }

    public abstract ItemStack bestMatch(Inventory target);
}
