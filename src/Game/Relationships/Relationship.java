package Game.Relationships;
import Game.Colonist.*;

import java.util.EnumMap;

public class Relationship {
    private Colonist other;      // the other colonist this relationship is with
    private String type;         // e.g. "Friend", "Rival", "Spouse"

    private EnumMap<RelationshipType, Integer> values;

    public Relationship(Colonist other, String type) {
        this.other = other;
        this.type = type;
        this.values = new EnumMap<>(RelationshipType.class);
        for (RelationshipType rtype : RelationshipType.values()) {
            values.put(rtype, 0);
        }
    }

    public Colonist getOther() {
        return other;
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
        values.put(type, values.get(type) + delta);
    }



    @Override
    public String toString() {
        return type + " with " + other.getName();
    }
}