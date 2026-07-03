package Game.Engine.Colonist.Career;

import Game.Engine.Colonist.Career.Job.Job;
import Game.Engine.Colonist.Career.Job.JobType;
import Game.Engine.Colonist.Career.Profession.Profession;
import Game.Engine.Colonist.Career.Profession.ProfessionRegistry;

import java.util.HashMap;


public class ColonistCareer {
    private Profession currentProf;

    private Job activeJob;

    private HashMap<JobType, Job> career = new HashMap<>();

    public ColonistCareer(){
        for(Profession prof: ProfessionRegistry.getAllProfessions()){
            career.put(prof.getJobType(),prof.createJob());
        }
    }
    public void setCurrentProf(Profession prof){
        currentProf = prof;
        activeJob = career.get(prof.getJobType());

    }

    public Profession getCurrentProf() {
        return currentProf;
    }

    public Job getActiveJob() {
        return activeJob;
    }
}
