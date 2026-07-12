package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Colonist.ActionManager;

public class MineAction extends WorkAction{
    public MineAction( ActionManager colonist) {
        super("Mine", colonist);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public void updateQueue() {

    }
}
