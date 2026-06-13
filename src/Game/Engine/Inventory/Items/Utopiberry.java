package Game.Engine.Inventory.Items;

import Game.Engine.Colonist.Colonist;

public class Utopiberry extends Item implements Consumable{
    public Utopiberry(String name, float weight) {
        super("UtopiBerry", 2);
    }

    @Override
    public void consume(Colonist colonist) {

    }
}
