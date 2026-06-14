package Game.Engine.Inventory.Items;

import Game.Engine.Actions.ColonistActions.ConsumeAction;
import Game.Engine.Colonist.Colonist;

public class Utopiberry extends Item implements Consumable{
    public Utopiberry() {

        super("UtopiBerry", 2,ItemType.FOOD);

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
