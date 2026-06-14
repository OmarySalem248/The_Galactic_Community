package Game.Engine.Actions.ColonistActions;



import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.*;


public class PickupAction extends ColonistAction {
    private final Inventory source;
    private final Item item;
    private final int quantity;

    public PickupAction(ActionManager am, Inventory source, Item item, int quantity) {
        super("Pickup", am);
        this.source   = source;
        this.item     = item;
        this.quantity = quantity;
    }

    @Override
    public boolean execute() {
        if (!source.hasItem(item, quantity)) return false;
        int added = colonist.getInventory().add(item, quantity);
        source.remove(item, added);
        return true;
    }
}