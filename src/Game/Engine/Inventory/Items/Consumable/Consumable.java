package Game.Engine.Inventory.Items.Consumable;

import Game.Engine.Colonist.Colonist;

public interface Consumable {


    /** Apply effect to the colonist who consumed this item. */
    void consume(Colonist colonist);

    int getTime();
}
