package Game.Actions;

import Game.Colonist.Colonist;
import Game.Colony;
import Game.Relationships.RelationshipType;

public class InteractAction extends Action{
    private Colonist colonist1;
    private Colonist colonist2;
    private RelationshipType type;
    private int intensity1;
    private int intensity2;


    public InteractAction(Colonist colonist1, Colonist colonist2, RelationshipType type,int intensity1,int intensity2){
        super(type.name() + "Interaction between " + colonist1.getName() + " and " + colonist2.getName());
        this.colonist1 = colonist1;
        this.colonist2 = colonist2;
        this.type = type;
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;

    }
    @Override
    public boolean execute(Colony colony) {

        if (!colonist1.getRelationships().hasRelationshipWith(colonist2.getName())) {
            colonist1.getRelationships().addRelationship(new Game.Relationships.Relationship(colonist2,"None"));
        }

        if (!colonist2.getRelationships().hasRelationshipWith(colonist1.getName())) {
            colonist2.getRelationships().addRelationship(new Game.Relationships.Relationship(colonist1,"None"));
        }

        // Apply interaction effects
        colonist1.getRelationships().adjustRelationship(colonist2.getName(), type, intensity1);
        colonist2.getRelationships().adjustRelationship(colonist1.getName(), type, intensity2);

        return true;
    }

}
