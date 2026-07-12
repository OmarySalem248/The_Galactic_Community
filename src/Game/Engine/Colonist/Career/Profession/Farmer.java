package Game.Engine.Colonist.Career.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.Farming.FarmAction;
import Game.Engine.Colonist.Career.Job.FarmJob;
import Game.Engine.Colonist.Career.Job.Job;
import Game.Engine.Colonist.Career.Job.JobType;
import Game.Engine.Time.GameTime;

public class Farmer extends Profession {
    public Farmer() {
        super("Famer", FarmAction.class, JobType.FARM);
    }


    public String getName() { return "Farmer"; }

    @Override
    public boolean isItWorkHours(GameTime time){
        return true;
    }

    @Override
    public Job createJob() {
        return new FarmJob();
    }


}