package Engine.Actions.Interactions;

import Engine.Colonist.Colonist;
import Engine.Relationships.Relationship;
import Engine.Relationships.RelationshipType;

public abstract class InteractAction{
    private Colonist colonist1;
    private Colonist colonist2;
    private RelationshipType type;



    public InteractAction(){

    }

    public abstract void execute(Colonist c1, Colonist c2, Relationship rel1,Relationship rel2);

}
