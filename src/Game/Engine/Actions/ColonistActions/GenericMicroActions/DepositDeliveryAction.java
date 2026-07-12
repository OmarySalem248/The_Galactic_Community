package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Inventory.Delivery;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;

/**
 * Deposits a specific delivery into its destination inventory.
 * Releases the transport claim on the source inventory once complete.
 * The calling code is responsible for identifying which delivery to deposit
 * (e.g. the one whose destination matches the current building's inventory).
 */
public class DepositDeliveryAction extends MicroAction {

    private final Delivery delivery;

    public DepositDeliveryAction(ActionManager am, Delivery delivery) {
        super("DepositDelivery", am);
        this.delivery = delivery;
    }

    @Override
    public boolean execute() {
        Inventory destination = delivery.getDestination();

        for (ItemStack stack : delivery.getItems()) {
            destination.add(stack.getItem(), stack.getQuantity());
        }

        // Remove delivery from colonist's list and release source claim
        colonist.getInventory().removeDelivery(delivery);
        delivery.getSource().releaseTransportClaim();

        return true;
    }
}
