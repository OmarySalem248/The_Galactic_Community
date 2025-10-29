package Game.Engine.Actions.Interactions;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Relationships.Relationship;
import Game.Engine.Relationships.RelationshipType;

public class FamilialAction extends InteractAction{
    public FamilialAction(){

    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        rel1.adjustValue(RelationshipType.FAMILIAL,2);
        rel2.adjustValue(RelationshipType.FAMILIAL,2);

    }
}
