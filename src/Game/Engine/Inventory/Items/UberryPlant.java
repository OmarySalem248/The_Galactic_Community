package Game.Engine.Inventory.Items;

public class UberryPlant extends Plant{
    public UberryPlant() {
        super("UtopiBerry Bush");
    }

    @Override
    protected void onBeginGrowth() {

    }

    @Override
    protected void growAmount() {

    }

    @Override
    public boolean isMatured() {
        return false;
    }
}
