package Game.Engine.Inventory.Items;

/**
 * Represents a growing plant inside a PlantIncubater.
 * Growth logic is left abstract — implement growAmount() and isMatured()
 * however fits your design (linear, stages, randomness, environmental factors etc.)
 */
public abstract class Plant {

    private final String name;
    private boolean growing = false;

    public Plant(String name) {
        this.name = name;
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
    protected abstract void growAmount();

    /** Return true once the plant is fully grown. */
    public abstract boolean isMatured();

    public boolean isGrowing() { return growing; }
    public String getName()    { return name; }
}
