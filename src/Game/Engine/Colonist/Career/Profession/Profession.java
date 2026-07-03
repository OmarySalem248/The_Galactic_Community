package Game.Engine.Colonist.Career.Profession;


import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Career.Job.GenericJob;
import Game.Engine.Colonist.Career.Job.Job;
import Game.Engine.Colonist.Career.Job.JobType;
import Game.Engine.Time.GameTime;


public abstract class Profession {
    private String name;
    private Class<? extends WorkAction> workAction;

    private JobType jobType;
    public  Profession(String name, Class<? extends WorkAction> workAction, JobType jobType){
        this.name = name;
        this.workAction = workAction;
        this.jobType = jobType;
    }

    public Class<? extends WorkAction> getWorkAction(){
        return workAction;
    }
    public void work(ActionManager am) {
        try {
            WorkAction action = workAction.getDeclaredConstructor(ActionManager.class).newInstance(am);
            action.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute work action for " + name, e);
        }
    }
    public String getName() {
        return name;
    }

    public JobType getJobType(){
        return jobType;
    }


    public boolean isItWorkHours(GameTime time) {
        return (time.hour()) >= 8 && time.hour() <= 17 && time.weekday() >= 1 && time.weekday() <= 5;
    }

    public  Job createJob(){
        return new GenericJob();
    }
}



