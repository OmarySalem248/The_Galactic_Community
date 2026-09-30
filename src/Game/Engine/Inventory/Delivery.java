package Game.Engine.Inventory;

import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;

import java.util.ArrayList;
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

    public Delivery() {
        this.items       =  new ArrayList<>();
        this.source      = null;
        this.destination = null;
    }



    public void setDestination(Inventory destination) { this.destination = destination; }

    public List<ItemStack> getItems()  { return items; }
    public Inventory getSource()       { return source; }
    public Inventory getDestination()  { return destination; }
    public void addItems(Item item, int quantity) {
        items.stream()
                .filter(s -> s.getItem().getClass() == item.getClass())
                .findFirst()
                .ifPresentOrElse(
                        s -> s.add(quantity),
                        () -> items.add(new ItemStack(item, quantity))
                );
    }

    public boolean isEmpty() {
        for(ItemStack stack: items){
            if(!stack.isEmpty()){
                return  false;
            }
        }
        return true;
    }
}