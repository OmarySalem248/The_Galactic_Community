package com.GalacticCommunity.model.Engine.Relationships;

import com.GalacticCommunity.model.Engine.Actions.Interactions.*;
import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colony;
import com.GalacticCommunity.model.Engine.Actions.Interactions.*;

import java.util.*;

import static com.GalacticCommunity.model.Engine.Relationships.RelationshipType.*;

public class RelationshipManager {
    private FlirtAction flirt;
    private ChitChatAction chitchat;
    private FamilialAction famillyaction;
    private Colony colony;

    private CoworkerInteract coworkerInteract;
    private IntercourseAction intercourse;
    private ProposeAction propose;
    private DateAction date;
    private MarryAction marry;
    private List<Colonist> colonists;


    public RelationshipManager(Colony colony){
        this.flirt = new FlirtAction();
        this.famillyaction = new FamilialAction();
        this.coworkerInteract = new CoworkerInteract();
        this.colony =colony;
        this.colonists = colony.getColonists();
        this.chitchat =new ChitChatAction();
        this.intercourse = new IntercourseAction();
        this.propose = new ProposeAction();
        this.date = new DateAction();
        this.marry = new MarryAction();

    }
    public void developRelationships() {
        Random rand = new Random();

        for (Colonist c1 : colonists) {
            if (!c1.isAlive()) continue;
            List<Colonist> candidates;


            candidates = selectCandidates(c1);



            for (Colonist c2 : candidates) {
                if (c1 == c2) continue;

                Relationship rel1 = c1.getRelationships().get(c2.getName());
                Relationship rel2 = c2.getRelationships().get(c1.getName());

                if (rel1 == null) {
                    rel1 = new Relationship(c1,c2,"None");
                    c1.getRelationships().addRelationship(rel1);
                }
                if (rel2 == null) {
                    rel2 = new Relationship(c2,c1,"None");
                    c2.getRelationships().addRelationship(rel2);
                }
                for (RelationshipType rtype : RelationshipType.values()) {
                    int val = rel1.getValues().get(rtype);
                    int adj = 0;
                    if (val > 0) adj = -1;
                    if (val < 0) adj = 1;
                    rel1.adjustValue(rtype, adj);
                }

                double flirtchance = rand.nextDouble() + (rel1.getValue(ROMANTIC)+rel2.getValue(ROMANTIC))/2000;
                if (areCompatible(c1, c2) && rel1.getValue(ROMANTIC) < 50 && !c1.getTaken() && !c2.getTaken() && flirtchance >0.5) {
                    flirt.execute(c1, c2, rel1, rel2);
                }
                double datechance = rand.nextDouble() + (rel1.getValue(ROMANTIC)+rel2.getValue(ROMANTIC))/4000;
                if (rel1.getValue(ROMANTIC) >= 45 && rel2.getValue(ROMANTIC) >= 45 && datechance > 0.5) {
                    date.execute(c1, c2, rel1, rel2);
                }
                if (rel1.getValue(ROMANTIC) >= 80 && rel2.getValue(ROMANTIC) >= 80 && !c1.isEngagedTo(c2)) {
                    propose.execute(c1, c2, rel1, rel2);
                }
                if (rel1.getValue(ROMANTIC) >= 100 && rel2.getValue(ROMANTIC) >= 100 && c1.isEngagedTo(c2)) {
                    marry.execute(c1, c2, rel1, rel2);
                } else if (c1.getAssignedBuilding() != null &&
                        c2.getAssignedBuilding() != null &&
                        c1.getAssignedBuilding() == c2.getAssignedBuilding()) {
                    coworkerInteract.execute(c1, c2, rel1, rel2);
                } else if ((c1.getBiofather() == c2) || (c1.getBiomother() == c2)) {
                    famillyaction.execute(c1, c2, rel1, rel2);
                } else {
                    chitchat.execute(c1, c2, rel1, rel2);
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

        List<String> deadRelationships = new ArrayList<>();
        for (Map.Entry<String, Relationship> entry : c1.getRelationships().entrySet()) {
            Relationship rel = entry.getValue();
            if (!rel.getOther().isAlive()) {
                deadRelationships.add(rel.getOtherName());
                continue;
            }
            int relsum = 0;

            for (RelationshipType rtype : RelationshipType.values()) {
                if (rtype != PROXIMITY) {
                    relsum += Math.abs(rel.getValue(rtype));
                }
            }

            if (relsum > 30) {
                Colonist strongBond = findColonistByName(entry.getKey());
                if (strongBond != null && strongBond.isAlive()) {
                    candidates.add(strongBond);
                }
            }

        }
        for (String deadName : deadRelationships) {
            c1.getRelationships().removeRelationshipWith(deadName);
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
        boolean isRelated = c1.isRelated(c2);

        int ageDiff = Math.abs(c1.getAge() - c2.getAge());
        boolean ageOK = (c1.getAge() > 18 && ageDiff <= 3) || (c1.getAge() > 35 && c2.getAge() > 35)||(c1.getAge()>25 && ageDiff<=7);

        return sexuallyAttracted && ageOK && !isRelated;
    }
}
