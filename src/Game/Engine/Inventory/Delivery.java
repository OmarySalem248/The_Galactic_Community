package Game.Engine.Inventory;


import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;

import java.util.List;
/*
Deliveries will be a segmented part of the inventory that is labeled as specifically to be taken to somewhere else and not for the colonists personal use
 */
public class Delivery {

    private final List<ItemStack> items;
    private final Inventory destination;

    public Delivery(List<ItemStack> items, Inventory destination) {
        this.items       = items;
        this.destination = destination;
    }

    public List<ItemStack> getItems()      { return items; }
    public Inventory getDestination()      { return destination; }
}