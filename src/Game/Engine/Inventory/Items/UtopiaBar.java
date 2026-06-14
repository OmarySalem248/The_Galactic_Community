package Game.Engine.Inventory.Items;

import Game.Engine.Colonist.Colonist;

public class UtopiaBar extends Item implements Consumable{
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
