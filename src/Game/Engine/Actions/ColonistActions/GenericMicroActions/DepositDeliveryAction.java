package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Inventory.Delivery;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;

import static java.lang.Math.floor;
import static java.lang.Math.min;

/**
 * Deposits a specific delivery into its destination inventory.
 * Releases the transport claim on the source inventory once complete.
 * The calling code is responsible for identifying which delivery to deposit
 * (e.g. the one whose destination matches the current building's inventory).
 */
public class DepositDeliveryAction extends MicroAction {

    private final Delivery delivery;

    private Inventory inventory;

    public DepositDeliveryAction(ActionManager am, Delivery delivery, Inventory inv) {
        System.out.println("dwd");
        super("DepositDelivery", am);
        this.delivery = delivery;
        this.inventory = inv;
    }

    @Override
    public boolean execute() {

        if(!delivery.isEmpty()){

            ItemStack stack = delivery.getItems().getFirst();
            Item item = stack.getItem();
            int quant = (int) min(floor(colonistam.getHandfull()/ item.getWeight()), stack.getQuantity());
            System.out.println(quant);
            stack.remove(quant);
            inventory.add(item,quant);
            return false;

        }
        return true;
    }

}
