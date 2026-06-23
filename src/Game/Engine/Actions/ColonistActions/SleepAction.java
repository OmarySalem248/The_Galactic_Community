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

        colonistam.sleep();


        tick++;
        if(tick%60 ==0) {
            colonist.modEnergy(50);
        }
        colonist.setStatus("zzzzzzzzzz");

        if ((colonist.getEnergy() >= 900 && colonistam.status().getshouldWork())||(colonist.getEnergy() >= 1000)){
            colonistam.wake();
            return true;
        }
        return false;
    }
}
