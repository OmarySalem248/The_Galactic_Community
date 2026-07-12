package Game.Engine.Actions.ColonistActions.GenericMicroActions.ConsumeActions;

import Game.Engine.Actions.ColonistActions.GenericMicroActions.MicroAction;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Items.Consumable.Consumable;

public class ConsumeAction  extends MicroAction {
    private int timetocon;

    private Consumable consumable;

    public ConsumeAction(ActionManager colonist, Consumable consumable) {
        super("Consume", colonist);
        this.consumable = consumable;
        this.timetocon = consumable.getTime();
    }

    @Override
    public boolean execute() {
        this.timetocon --;
        consumable.consume(colonist);
        return timetocon == 0;
    }
}
