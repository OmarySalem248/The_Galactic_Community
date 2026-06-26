package Game.Engine.Inventory;

import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;

import java.util.List;

/**
 * Represents a work-related transport task — items claimed from a source
 * inventory with an intended destination. Kept separate from personal inventory
 * so colonists don't mix work goods with their own belongings.
 */
public class Delivery {

    private final List<ItemStack> items;
    private final Inventory source;
    private Inventory destination;

    public Delivery(List<ItemStack> items, Inventory source, Inventory destination) {
        this.items       = items;
        this.source      = source;
        this.destination = destination;
    }
    public void setDestination(Inventory destination) { this.destination = destination; }

    public List<ItemStack> getItems()  { return items; }
    public Inventory getSource()       { return source; }
    public Inventory getDestination()  { return destination; }
}