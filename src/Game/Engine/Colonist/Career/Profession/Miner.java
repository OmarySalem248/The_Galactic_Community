package Game.Engine.Colonist.Career.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.MineAction;
import Game.Engine.Colonist.Career.Job.JobType;

public class Miner extends Profession {
    public Miner() {
        super("Miner", MineAction.class, JobType.MINE);
    }


    public String getName() { return "Miner"; }
}
