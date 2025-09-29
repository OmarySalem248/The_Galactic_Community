package Game.Relationships;
import Game.Colonist.*;

import java.util.EnumMap;

public class Relationship {
    private Colonist other;      // the other colonist this relationship is with
    private String type;         // e.g. "Friend", "Rival", "Spouse"

    private Colonist owner;

    private boolean marriagestatus;

    private EnumMap<RelationshipType, Integer> values;

    public Relationship(Colonist me,Colonist other, String type) {
        this.other = other;
        this.type = type;
        this.values = new EnumMap<>(RelationshipType.class);
        for (RelationshipType rtype : RelationshipType.values()) {
            values.put(rtype, 0);
        }
        this.marriagestatus = false;
        this.owner = me;
    }

    public Colonist getOther() {
        return other;
    }
    public void setType(String type){
        this.type = type;

    }
    public  EnumMap<RelationshipType, Integer> getValues(){
        return values;
    }

    public String getOtherName() {
        return other.getName();
    }

    public String getType() {
        return type;
    }


    public void setValue(RelationshipType type, int value) {
        values.put(type, value);
    }

    public void adjustValue(RelationshipType type, int delta) {
        int current = values.get(type);
        current += delta;
        values.put(type, current);


        switch (type) {
            case PLATONIC:
                if (current >= 80) setType("Best Friends");
                else if (current >= 50) setType("Friends");
                else if (current <= -50) setType("Enemies");
                else if (current <= -80) setType("Nemisis");
                break;

            case FAMILIAL:
                if (current < 0) setType("Poor Family Relationship");
                if (current >= 0) setType("Family");
                if (current >= 70) setType("Strong Family Bond");
                break;

            case ROMANTIC:
                int feelingdifference = current - other.getRelationships().get(owner.getName()).getValue(RelationshipType.ROMANTIC);
                if (feelingdifference > 10) setType("Unrequited Love");
                if (feelingdifference < -10) setType("Stalker");
                if(Math.abs(feelingdifference)<10) {
                    setType("None");
                    if(current>=50 && owner.getPersonality().getMono() && other.getPersonality().getMono()){
                            owner.setTaken(true);
                            other.setTaken(true);
                    }
                    if (current >= 50 && !marriagestatus){
                        setType("Lover");
                    }
                    if (current >= 80 && !marriagestatus) setType("Engaged");
                }
                if(current < 45){
                    owner.setTaken(false);
                    other.setTaken(false);
                }

                break;

            case SEXUAL:
                if (current >= -50) setType("Disgusted");
                if (current >= 50) setType("Attracted");
                break;
        }
    }



    @Override
    public String toString() {
        return type + " with " + other.getName();
    }

    public int getValue(RelationshipType relationshipType) {
        return values.getOrDefault(relationshipType,0);
    }
}