package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Actions.ColonistActions.GenericMicroActions.MicroAction;

public class SleepAction  extends MicroAction {
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

        if ((colonist.getEnergy() >= 900 && colonistam.status().getshouldWork() && !colonistam.getWorkDone())||(colonist.getEnergy() >= 1000)){
            System.out.println(colonist.getName() + " wakes up");
            colonistam.wake();
            return true;
        }
        return false;
    }
}
