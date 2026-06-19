package Game.Engine.Actions.ColonistActions;

import Game.Engine.Colonist.ActionManager;

public class SleepAction extends ColonistAction{
    private int tick;
    public SleepAction(ActionManager colonist) {
        super("Sleep", colonist);
        tick = 0;

    }

    @Override
    public boolean execute() {
        tick++;
        if(tick%60 ==0) {
            colonist.modEnergy(50);
        }
        colonist.setStatus("zzzzzzzzzz");

        return colonist.getEnergy() < 90;
    }
}
