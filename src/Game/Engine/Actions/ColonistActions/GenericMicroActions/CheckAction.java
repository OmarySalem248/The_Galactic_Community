package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Memory.Search.Search;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;

public class CheckAction extends MicroAction{
    public CheckAction(ActionManager colonist, Inventory target, Search search) {
        super("Checking", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
