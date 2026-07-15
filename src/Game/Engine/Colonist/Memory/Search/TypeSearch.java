package Game.Engine.Colonist.Memory.Search;

import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemType;

/**
 * Search for any item of a given ItemType e.g. ItemType.SEED, ItemType.FOOD
 */
public class TypeSearch extends Search {

    private final ItemType type;

    public TypeSearch(int quant,ItemType type) {
        super(quant);
        this.type = type;
    }

    public ItemType getType() { return type; }

    @Override
    public float getSuitability(Inventory inv) {
        if(!inv.hasType(type)){
            return 0;
        }

        return (float) getQuantity() /inv.getByType(type).size();
    }

    @Override
    public boolean matches(Item item) {
        return item.getType() == type;
    }

    @Override
    public String describe() {
        return type.name();
    }
}
