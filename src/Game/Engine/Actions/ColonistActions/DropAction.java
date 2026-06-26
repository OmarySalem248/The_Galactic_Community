package Game.Engine.Actions.ColonistActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;

public class DropAction extends ColonistAction {

    private final Inventory source; // the originating inventory (e.g. farm inv)
    private final Inventory target; // the destination inventory (e.g. storage inv)
    private final Item item;
    private final int quantity;

    public DropAction(ActionManager am, Inventory source, Inventory target, Item item, int quantity) {
        super("Drop", am);
        this.source   = source;
        this.target   = target;
        this.item     = item;
        this.quantity = quantity;
    }

    @Override
    public boolean execute() {
        if (!colonist.getInventory().hasItem(item, quantity)) return false;
        int added = target.add(item, quantity);
        colonist.getInventory().remove(item, added);
        // Release the transport claim on the source inventory now that goods are delivered
        source.releaseTransportClaim();
        return true;
    }
}