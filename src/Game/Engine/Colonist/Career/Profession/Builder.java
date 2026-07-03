package Game.Engine.Colonist.Career.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.EngineerCheckAction;
import Game.Engine.Colonist.Career.Job.BuildJob;
import Game.Engine.Colonist.Career.Job.Job;
import Game.Engine.Colonist.Career.Job.JobType;

public class Builder extends Profession {


    public Builder() {
        super("Builder", EngineerCheckAction.class, JobType.BUILD);
    }

    @Override
    public String getName() {
        return "Builder";
    }

    @Override
    public Job createJob() {
        return new BuildJob();
    }


}
