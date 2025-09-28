package Game.Actions.Interactions;

import Game.Colonist.Colonist;
import Game.Relationships.Relationship;
import Game.Relationships.RelationshipType;

public class CoworkerInteract extends InteractAction{
    public CoworkerInteract(){

    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        rel1.adjustValue(RelationshipType.PLATONIC,1);
        rel2.adjustValue(RelationshipType.PLATONIC,1);
    }
}
