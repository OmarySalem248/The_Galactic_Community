package Engine.Relationships;
import Engine.Colonist.*;

import java.util.EnumMap;

public class Relationship {
    private Colonist other;      // the other colonist this relationship is with
    private String type;

    private Colonist owner;

    private boolean marriagestatus;

    private EnumMap<RelationshipType, Integer> values;
    private EnumMap<RelationshipType, String> types;

    public Relationship(Colonist me,Colonist other, String type) {
        this.other = other;
        this.type = type;
        this.values = new EnumMap<>(RelationshipType.class);
        this.types = new EnumMap<>(RelationshipType.class);
        for (RelationshipType rtype : RelationshipType.values()) {
            values.put(rtype, 0);
            types.put(rtype,type);
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
        int strongestrel = 0;
        String type = this.type;

        for(RelationshipType relationshipType: RelationshipType.values()){
            if(Math.abs(values.get(relationshipType))> strongestrel && relationshipType != RelationshipType.PROXIMITY){
                type = types.get(relationshipType);
                strongestrel = Math.abs(values.get(relationshipType));
                System.out.print(strongestrel);
            }

        }
        return type;
    }

    public String getTypeAdv(RelationshipType type){
        return types.get(type);
    }


    public void setValue(RelationshipType type, int value) {
        values.put(type, value);
    }

    public void setTypeAdv(RelationshipType type,String type2){
        this.types.put(type,type2);
    }

    public void adjustValue(RelationshipType type, int delta) {
        int current = values.get(type);
        current += delta;
        values.put(type, current);
        RelationshipType t;

        switch (type) {
            case PLATONIC:
                t = RelationshipType.PLATONIC;
                if (current >= 80) setTypeAdv(t,"Best Friends");
                else if (current >= 50) setTypeAdv(t,"Friends");
                else if (current <= -50) setTypeAdv(t,"Enemies");
                else if (current <= -80) setTypeAdv(t,"Nemisis");
                break;

            case FAMILIAL:
                t = RelationshipType.FAMILIAL;
                if (current < 0) setTypeAdv(t,"Poor Family Relationship");
                if (current >= 70) setTypeAdv(t,"Strong Family Bond");
                break;

            case ROMANTIC:
                t = RelationshipType.ROMANTIC;
                int otherVal = other.getRelationships()
                        .get(owner.getName())
                        .getValue(RelationshipType.ROMANTIC);
                int feelingDiff = current - otherVal;


                if (feelingDiff > 20) {
                    setTypeAdv(t,"Unrequited Love");
                } else if (feelingDiff < -20) {
                    setTypeAdv(t,"Stalker");
                } else {

                    if (current >= 80 && !marriagestatus) {
                        owner.setTaken(true);
                        other.setTaken(true);
                    } else if (current >= 50) {

                        setTypeAdv(t,"Lover");
                        if (owner.getPersonality().getMono() && other.getPersonality().getMono()) {
                            owner.setTaken(true);
                            other.setTaken(true);
                        }
                    }
                }


                if (current < 40) {
                    if (marriagestatus || getTypeAdv(RelationshipType.ROMANTIC).equals("Lover") || getTypeAdv(RelationshipType.ROMANTIC).equals("Engaged")) {

                        owner.setTaken(false);
                        other.setTaken(false);
                        setTypeAdv(t,"Broken Up");
                    };
                }
                break;


            case SEXUAL:
                t = RelationshipType.SEXUAL;
                if (current <= -20) setTypeAdv(t,"Disgusted");
                if (current >= 20) setTypeAdv(t,"Attracted");
                break;
            case ADMIRATION:
                t = RelationshipType.ADMIRATION;
                if (current >= 30) setTypeAdv(t,"Idol");
                if(current >= 50) setTypeAdv(t,"Hero");
                if (current >= 500) setTypeAdv(t,"Prophet");
                if (current >= 1000) setTypeAdv(t,"God");
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