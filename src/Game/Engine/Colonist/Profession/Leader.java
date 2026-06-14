package Game.Engine.Colonist.Profession;


import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.Colonist;

public abstract class Leader extends Profession{

    Colonist successor;

    public Leader(String name, Class<? extends WorkAction> workAction) {
        super(name, workAction);
    }

    public Colonist getSuccessor(){
        return successor;
    }


    public void setSuccessor(Colonist successor){
        this.successor = successor;
    }
}
