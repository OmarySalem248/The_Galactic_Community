package Game.Engine.Inventory.Items.Plant;

import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;

import java.util.List;

/**
 * Represents a growing plant inside a PlantIncubater.
 * Growth logic is left abstract — implement growAmount() and isMatured()
 * however fits your design (linear, stages, randomness, environmental factors etc.)
 */
public abstract class Plant {
    private int progress;
    private int MATURE_AT;

    private int delay;

    private Item product;

    private final String name;

    private int dailyMain;
    private boolean growing = false;

    public Plant(String name, int dailyMain, int delay,int matureAt,Item product) {
        MATURE_AT = matureAt;
        this.name = name;
        this.delay = delay;
        this.dailyMain = dailyMain;
        this.progress = 0;
        this.product = product;
    }

    public void beginGrowth() {
        growing = true;
        onBeginGrowth();
    }

    /** Called every minute while growing — advance growth however you like. */
    public final void tick() {
        if (!growing || isMatured()) return;
        growAmount();
        if (isMatured()) {
            growing = false;
        }
    }

    /** Called once when growth starts — reset any progress fields here. */
    protected abstract void onBeginGrowth();

    /** Called every tick while growing — advance your own progress state. */
    protected void growAmount(){
        progress++;
    }


    /** Return true once the plant is fully grown. */
    public boolean isMatured()     { return progress >= MATURE_AT; }

    public boolean isGrowing() { return growing; }
    public String getName()    { return name; }

    public Item getProduct() {
        return product;
    }

    public abstract List<ItemStack> getHarvestYield();

    public int getDelay() {
        return delay;
    }

    public int getDailyMain(){
        return dailyMain;
    }
}
