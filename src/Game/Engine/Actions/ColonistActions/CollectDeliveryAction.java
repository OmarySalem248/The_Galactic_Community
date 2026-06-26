package Game.Engine.Actions.ColonistActions;

import Game.Engine.Inventory.Delivery;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects items from a source inventory as a work delivery.
 * Claims the source inventory to prevent other colonists from collecting the same items.
 * Items are tracked as a Delivery in the colonist's inventory, separate from personal items.
 */
public class CollectDeliveryAction extends ColonistAction {

    private final Inventory source;
    private final Inventory destination;
    private final ItemType type;
    private final int quantity;

    public CollectDeliveryAction(ActionManager am, Inventory source, Inventory destination,
                                  ItemType type, int quantity) {
        super("CollectDelivery", am);
        this.source      = source;
        this.destination = destination;
        this.type        = type;
        this.quantity    = quantity;
    }

    @Override
    public boolean execute() {
        if (!source.hasAvailableType(type)) return false;
        if (!source.claimTransport()) return false;

        // Collect items from source into a delivery list
        List<ItemStack> collected = new ArrayList<>();
        List<ItemStack> available = new ArrayList<>(source.getByType(type));

        int remaining = quantity;
        for (ItemStack stack : available) {
            int toTake = Math.min(stack.getQuantity(), remaining);
            if (toTake <= 0) continue;
            if (source.remove(stack.getItem(), toTake)) {
                collected.add(new ItemStack(stack.getItem(), toTake));
                remaining -= toTake;
            }
            if (remaining <= 0) break;
        }

        if (collected.isEmpty()) {
            source.releaseTransportClaim();
            return false;
        }

        // Register as a delivery in the colonist's inventory
        Delivery delivery = new Delivery(collected, source, destination);
        colonist.getInventory().addDelivery(delivery);

        // Claim stays held until DepositDeliveryAction releases it on arrival
        return true;
    }
}
