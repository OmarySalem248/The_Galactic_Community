package Game.Engine.Inventory.Items.Plant;

import Game.Engine.Inventory.Items.Consumable.Food.Utopiberry;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.Seed.UberrySeed;

import java.util.List;

public class UberryPlant extends Plant {
    /*
    168
     */
    public UberryPlant() {
        super("UtopiBerry Bush", 2,60,24);

    }

    @Override
    protected void onBeginGrowth() {

    }




    @Override
    public List<ItemStack> getHarvestYield() {
        return List.of(
                new ItemStack(new Utopiberry(), 5),
                new ItemStack(new UberrySeed(), 2)
        );
    }
}
