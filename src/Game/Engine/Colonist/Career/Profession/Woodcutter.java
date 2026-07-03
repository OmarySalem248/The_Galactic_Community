package Game.Engine.Colonist.Career.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.WoodCutAction;
import Game.Engine.Colonist.Career.Job.JobType;

public class Woodcutter extends Profession{
    public Woodcutter() {
        super("WoodCutter", WoodCutAction.class, JobType.CUT);
    }



}
