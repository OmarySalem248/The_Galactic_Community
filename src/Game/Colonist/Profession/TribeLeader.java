package Game.Colonist.Profession;

import Game.Actions.Interactions.LeaderSpeech;
import Game.Colonist.Colonist;
import Game.Relationships.RelationshipType;
import Game.Resources;

public class TribeLeader extends Leader{




    @Override
    public Resources work(Colonist colonist, int usedEnergy) {
        colonist.getColony().getLeadership().getGovernment().assignJobs();
        for(Colonist subject: colonist.getColony().getColonists()){
            if(subject != colonist && subject.getAge()>18) {
                speech.execute(colonist, subject, colonist.getRelationships().get(subject.getName()), subject.getRelationships().get(colonist.getName()));
                if(successor == null || !successor.isAlive()){
                    setSuccessor(subject);
                }
                if(subject.getRelationships().get(colonist.getName()).getValue(RelationshipType.ADMIRATION)>successor.getRelationships().get(colonist.getName()).getValue(RelationshipType.ADMIRATION)){
                    setSuccessor(subject);
                }
            }
        }
        return null;
    }



    @Override
    public String getName() {
        return "Tribe Leader";
    }


}
