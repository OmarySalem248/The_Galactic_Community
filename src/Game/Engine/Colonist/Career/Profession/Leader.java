package Game.Engine.Colonist.Career.Profession;


import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.Career.Job.JobType;
import Game.Engine.Colonist.Colonist;

public abstract class Leader extends Profession{

    Colonist successor;

    public Leader(String name, Class<? extends WorkAction> workAction) {
        super(name, workAction, JobType.LEAD);
    }

    public Colonist getSuccessor(){
        return successor;
    }


    public void setSuccessor(Colonist successor){
        this.successor = successor;
    }
}
