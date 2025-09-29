package Game.Actions.Interactions;

import Game.Colonist.Colonist;
import Game.Relationships.Relationship;
import Game.Relationships.RelationshipType;

public class FamilialAction extends InteractAction{
    public FamilialAction(){

    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        rel1.adjustValue(RelationshipType.FAMILIAL,2);
        rel2.adjustValue(RelationshipType.FAMILIAL,2);

    }
}
