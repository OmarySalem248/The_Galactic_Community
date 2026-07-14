package Game.Engine.Colonist.Memory.Search;

import Game.Engine.Inventory.Items.Item;

/**
 * Search for a specific item class e.g. Wood.class, Stone.class
 */
public class ClassSearch extends Search {

    private final Class<? extends Item> itemClass;

    public ClassSearch(int quant, Class<? extends Item> itemClass) {
        super(quant);
        this.itemClass = itemClass;
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
}
