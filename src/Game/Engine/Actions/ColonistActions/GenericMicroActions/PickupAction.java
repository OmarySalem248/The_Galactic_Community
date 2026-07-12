package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;

public class PickupAction extends MicroAction {

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
        if (!source.hasAvailableType(item.getType())) return false;
        if (!source.hasItem(item, quantity)) return false;
        if (!source.claimTransport()) return false; // another colonist already claimed it
        int added = colonist.getInventory().add(item, quantity);
        source.remove(item, added);
        source.releaseTransportClaim(); // release immediately after pickup
        return true;
    }
}
