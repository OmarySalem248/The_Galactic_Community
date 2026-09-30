package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.Memory.Search.Search;
import Game.Engine.Inventory.Delivery;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * Collects items from a source inventory as a work delivery.
 * Claims the source inventory to prevent other colonists from collecting the same items.
 * Items are tracked as a Delivery in the colonist's inventory, separate from personal items.
 */
public class CollectDeliveryAction extends MicroAction {


    private final Inventory target;
    private Search search;
    private Delivery delivery;

    public CollectDeliveryAction(ActionManager am, Delivery delivery, Inventory target, Search search) {
        super("CollectDelivery", am);
        this.target = target;
        this.delivery = delivery;
        this.search = search;
    }


    @Override
    public boolean execute() {
        if(search.invMatches(target)){
            ItemStack stack = search.bestMatch(target);
            Item item = stack.getItem();
            int quant = (int) min(floor(colonistam.getHandfull()/ item.getWeight()), stack.getQuantity());
            stack.remove(quant);
            delivery.addItems(item,quant);
            return false;

        }
        return true;

    }
}


