package Game.Engine.Colonist.Profession;


import Game.Engine.Colonist.Colonist;

public abstract class Leader implements Profession{

    Colonist successor;
    public Colonist getSuccessor(){
        return successor;
    }


    public void setSuccessor(Colonist successor){
        this.successor = successor;
    }
}
