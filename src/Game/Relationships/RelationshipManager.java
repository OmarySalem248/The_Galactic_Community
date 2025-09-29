package Game.Relationships;

import Game.Actions.Interactions.CoworkerInteract;
import Game.Actions.Interactions.FamilialAction;
import Game.Actions.Interactions.FlirtAction;
import Game.Actions.Interactions.InteractAction;
import Game.Colonist.Colonist;
import Game.Colony;

import java.util.*;

import static Game.Relationships.RelationshipType.*;

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
        Random rand = new Random();

        for (Colonist c1 : colonists) {
            if (!c1.isAlive()) continue;
            List<Colonist> candidates;


            candidates = selectCandidates(c1);
            System.out.print(c1);
            System.out.print(candidates);



            for (Colonist c2 : candidates) {
                if (c1 == c2) continue;

                Relationship rel1 = c1.getRelationships().get(c2.getName());
                Relationship rel2 = c2.getRelationships().get(c1.getName());


                for (RelationshipType rtype : RelationshipType.values()) {
                    int val = rel1.getValues().get(rtype);
                    if (val > 0) val -= 1;
                    if (val < 0) val += 1;
                    rel1.setValue(rtype, val);
                }


                if (areCompatible(c1, c2) && rel1.getValue(ROMANTIC)<50&& !c1.getTaken() && !c2.getTaken()) {
                    flirt.execute(c1, c2, rel1, rel2);
                }
                if (c1.getAssignedBuilding() != null &&
                        c2.getAssignedBuilding() != null &&
                        c1.getAssignedBuilding() == c2.getAssignedBuilding()) {
                    coworkerInteract.execute(c1, c2, rel1, rel2);
                }
                if ((c1.getBiofather() == c2) || (c1.getBiomother() == c2)) {
                    famillyaction.execute(c1, c2, rel1, rel2);
                }
            }
        }
    }

    private List<Colonist> selectCandidates(Colonist c1){
        Random rand = new Random();
        List<Colonist> candidates = new ArrayList<>();
        if (c1.getAssignedBuilding() != null) {
            candidates.addAll(c1.getAssignedBuilding().getColonists());
        }


        if (c1.getBiofather() != null) candidates.add(c1.getBiofather());
        if (c1.getBiomother() != null) candidates.add(c1.getBiomother());
        candidates.addAll(c1.getChildren());


        for (Map.Entry<String, Relationship> entry : c1.getRelationships().entrySet()) {
            Relationship rel = entry.getValue();
            int relsum = 0;

            for (RelationshipType rtype : RelationshipType.values()) {
                if (rtype != PROXIMITY) {
                    relsum += Math.abs(rel.getValue(rtype));
                }
            }

            if (relsum > 20) {
                Colonist strongBond = findColonistByName(entry.getKey());
                if (strongBond != null && strongBond.isAlive()) {
                    candidates.add(strongBond);
                }
            }

            if(candidates.size()<colonists.size()-1) {
                while (true) {
                    Colonist random = colonists.get(rand.nextInt(colonists.size()));
                    if (random != c1 && random.isAlive() && !candidates.contains(random)) {
                        candidates.add(random);
                        break;
                    }
                }
            }
        }

        return candidates;
    }


    private Colonist findColonistByName(String name) {
        for (Colonist c : colonists) {
            if (c.getName() == name) return c;
        }
        return null;
    }
    public boolean areCompatible(Colonist c1, Colonist c2) {

        boolean sexuallyAttracted = c1.isAttractedTo(c2) && c2.isAttractedTo(c1);

        int ageDiff = Math.abs(c1.getAge() - c2.getAge());
        boolean ageOK = (c1.getAge() > 18 && ageDiff <= 3) || (c1.getAge() > 35 && c2.getAge() > 35)||(c1.getAge()>25 && ageDiff<=7);

        return sexuallyAttracted && ageOK;
    }
}
