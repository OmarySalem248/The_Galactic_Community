package Game.Engine.Colonist.Career.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.Career.Job.JobType;

public class Engineer extends Profession{

    public Engineer(String name, Class<? extends WorkAction> workAction) {
        super(name, workAction, JobType.ENGINEER);
    }

    @Override
    public String getName() {
        return null;
    }
}
