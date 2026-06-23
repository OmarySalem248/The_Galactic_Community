package Game.Engine.Actions.ColonistActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Inventory.Items.Consumable.Consumable;

public class ConsumeAction extends ColonistAction{
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
