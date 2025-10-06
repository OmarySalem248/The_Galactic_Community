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
        if(this != null){return values;}
        return null;
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
                if (current >= 70) setType("Strong Family Bond");
                break;

            case ROMANTIC:
                int otherVal = other.getRelationships()
                        .get(owner.getName())
                        .getValue(RelationshipType.ROMANTIC);
                int feelingDiff = current - otherVal;


                if (feelingDiff > 20) {
                    setType("Unrequited Love");
                } else if (feelingDiff < -20) {
                    setType("Stalker");
                } else {

                    if (current >= 80 && !marriagestatus) {
                        owner.setTaken(true);
                        other.setTaken(true);
                    } else if (current >= 50) {

                        setType("Lover");
                        if (owner.getPersonality().getMono() && other.getPersonality().getMono()) {
                            owner.setTaken(true);
                            other.setTaken(true);
                        }
                    }
                }


                if (current < 40) {
                    if (marriagestatus || getType().equals("Lover") || getType().equals("Engaged")) {
                        owner.setTaken(false);
                        other.setTaken(false);
                        setType("Broken Up");
                    };
                }
                break;


            case SEXUAL:
                if (current <= -20) setType("Disgusted");
                if (current >= 20) setType("Attracted");
                break;
        }
    }



    @Override
    public String toString() {
        return type + " with " + other.getName();
    }

    public void setMarriagestatus(Boolean mstatus){
        marriagestatus =mstatus;
    }

    public boolean isMarriagestatus() {
        return marriagestatus;
    }

    public int getValue(RelationshipType relationshipType) {
        return values.getOrDefault(relationshipType,0);
    }


}