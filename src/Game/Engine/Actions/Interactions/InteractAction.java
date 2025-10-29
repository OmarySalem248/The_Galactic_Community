package Game.Engine.Actions.Interactions;

import Game.Engine.Relationships.RelationshipType;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Relationships.Relationship;

public abstract class InteractAction{
    private Colonist colonist1;
    private Colonist colonist2;
    private RelationshipType type;



    public InteractAction(){

    }

    public abstract void execute(Colonist c1, Colonist c2, Relationship rel1,Relationship rel2);

}
