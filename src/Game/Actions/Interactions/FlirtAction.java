package Game.Actions.Interactions;

import Game.Colonist.Colonist;
import Game.Relationships.Relationship;

import static Game.Relationships.RelationshipType.ROMANTIC;
import static Game.Relationships.RelationshipType.SEXUAL;

public class FlirtAction extends InteractAction {
    public FlirtAction() {
    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1,Relationship rel2){

        double randflirt = Math.random()*100;
        randflirt += c1.getRelationships().get(c2.getName()).getValue(ROMANTIC);
        randflirt += c2.getRelationships().get(c1.getName()).getValue(ROMANTIC);
        if (randflirt > 80 && randflirt < 90) {
            rel1.adjustValue(ROMANTIC,5);
            rel2.adjustValue(ROMANTIC,-10);
            rel2.adjustValue(SEXUAL,-1);
            c1.setStatus("Just blew it with "+c2.getName()+" :((((((((((((");
            c2.setStatus(c1.getName()+" is such a creep");
        } else if (randflirt > 90) {
            rel1.adjustValue(ROMANTIC,10);
            rel2.adjustValue(ROMANTIC,10);
            c1.setStatus(c2.getName()+" seemed really into me!!!");
            c2.setStatus(c1.getName()+" is such a flirt ;)");
            if(rel1.getValue(ROMANTIC)>50 && rel2.getValue(ROMANTIC)>50){
                rel1.adjustValue(SEXUAL,5);
                rel2.adjustValue(SEXUAL,5);
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
