package Game.Engine.Inventory.Items.Consumable.Food;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Items.Consumable.Consumable;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemType;

public class Utopiberry extends Item implements Consumable {
    public Utopiberry() {

        super("UtopiBerry", 2, ItemType.FOOD);

    }

    @Override
    public void consume(Colonist colonist) {
        colonist.modHunger(-2);


    }

    @Override
    public int getTime() {
        return 5;
    }
}
