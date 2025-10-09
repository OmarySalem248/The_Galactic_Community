package com.GalacticCommunity.model.Engine.Actions.Interactions;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Relationships.Relationship;
import com.GalacticCommunity.model.Engine.Relationships.RelationshipType;

import java.util.Random;

public class ProposeAction extends InteractAction{
    public ProposeAction(){

    }
    @Override
    public void execute(Colonist c1, Colonist c2, Relationship rel1, Relationship rel2) {
        if (c1.isMarried() || c2.isMarried()) return;
        if (c1.isEngagedTo(c2)) return;



        Random rand = new Random();
        int romantic = (rel1.getValue(RelationshipType.ROMANTIC) + rel2.getValue(RelationshipType.ROMANTIC)) / 2;
        int sexual = (rel1.getValue(RelationshipType.SEXUAL) + rel2.getValue(RelationshipType.SEXUAL)) / 2;
        int baseChance = romantic + sexual / 2;



        boolean accepted = rand.nextInt(100) < baseChance;

        if (accepted) {

            c1.setEngagedTo(c2);
            c2.setEngagedTo(c1);

            rel1.adjustValue(RelationshipType.ROMANTIC, 10);
            rel2.adjustValue(RelationshipType.ROMANTIC, 10);



        } else {
            rel1.adjustValue(RelationshipType.ROMANTIC, -10);
            rel2.adjustValue(RelationshipType.ROMANTIC, -5);
        }
    }

}
