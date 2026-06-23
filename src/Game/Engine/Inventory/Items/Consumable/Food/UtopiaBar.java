package Game.Engine.Inventory.Items.Consumable.Food;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Items.Consumable.Consumable;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemType;

public class UtopiaBar extends Item implements Consumable {
    public UtopiaBar() {
        super("UtopiaBar", 2, ItemType.FOOD);
    }

    @Override
    public void consume(Colonist colonist) {

    }

    @Override
    public int getTime() {
        return 4;
    }
}
