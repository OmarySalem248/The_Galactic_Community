package Game.Relationships;

import Game.Actions.Interactions.CoworkerInteract;
import Game.Actions.Interactions.FamilialAction;
import Game.Actions.Interactions.FlirtAction;
import Game.Actions.Interactions.InteractAction;
import Game.Colonist.Colonist;
import Game.Colony;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Game.Relationships.RelationshipType.PLATONIC;

public class RelationshipManager {
    private FlirtAction flirt;
    private FamilialAction famillyaction;
    private Colony colony;

    private CoworkerInteract coworkerInteract;
    private List<Colonist> colonists;


    public RelationshipManager(Colony colony){
        this.flirt = new FlirtAction();
        this.famillyaction = new FamilialAction();
        this.coworkerInteract = new CoworkerInteract();
        this.colony =colony;
        this.colonists = colony.getColonists();

    }
    public void developRelationships() {
        ArrayList<RelationshipType> rtypes = new ArrayList<>();
        for (RelationshipType rtype : RelationshipType.values()) {
            rtypes.add(rtype);
        }
        Random rand = new Random();

        for (Colonist c1 : colonists) {
            for (Colonist c2 : colonists) {
                if (c1 == c2) continue;


                Relationship rel1 = c1.getRelationships().get(c2.getName());
                Relationship rel2 = c2.getRelationships().get(c1.getName());


                for (RelationshipType rtype : RelationshipType.values()) {
                    int val = rel1.getValues().get(rtype);
                    if (val > 0) val -= 1;
                    if (val < 0) val += 1;
                    rel1.setValue(rtype, val);
                }

                if(areCompatible(c1,c2)){
                    flirt.execute(c1,c2,rel1,rel2);
                }


                if (c1.getAssignedBuilding() != null &&
                        c2.getAssignedBuilding() != null &&
                        c1.getAssignedBuilding() == c2.getAssignedBuilding()) {
                    coworkerInteract.execute(c1,c2,rel1,rel2);
                }


                if (c1.getBiofather() != null &&
                        c1.getBiomother() != null &&
                        c1.getBiofather() == c2 || c1.getBiomother() == c2) {
                        famillyaction.execute(c1,c2,rel1,rel2);

                }
            }
        }
    }

    public boolean areCompatible(Colonist c1, Colonist c2) {

        boolean sexuallyAttracted = c1.isAttractedTo(c2) && c2.isAttractedTo(c1);

        int ageDiff = Math.abs(c1.getAge() - c2.getAge());
        boolean ageOK = (c1.getAge() > 18 && ageDiff <= 3) || (c1.getAge() > 35 && c2.getAge() > 35)||(c1.getAge()>25 && ageDiff<=7);

        return sexuallyAttracted && ageOK;
    }
}
