package Game.Engine.Colonist.Memory.Search;

import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.Tiles.Coords;

/**
 * Search for a specific item class e.g. Wood.class, Stone.class
 */
public class ClassSearch extends Search {

    private final Class<? extends Item> itemClass;

    public ClassSearch(int quant, Coords origin, Coords dest,Class<? extends Item> itemClass) {
        super(quant, origin, dest);
        this.itemClass = itemClass;
    }
    @Override
    public float getSuitability(Inventory inv) {
        if(!inv.hasClass(itemClass)){
            return 0;
        }

        return (float) getQuantity() /inv.getByClass(itemClass).size();
    }


    public Class<? extends Item> getItemClass() { return itemClass; }

    @Override
    public boolean matches(Item item) {
        return item.getClass() == itemClass;
    }

    @Override
    public String describe() {
        return itemClass.getSimpleName();
    }
    @Override
    public boolean invMatches(Inventory inv) {
        for(ItemStack stack: inv.getStacks()){
            if(stack.getItem().getClass() == itemClass){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack bestMatch(Inventory inv){
        for(ItemStack stack: inv.getStacks()){
            if(stack.getItem().getClass() == itemClass){
                return stack;
            }
        }
        return null;

    }
}
