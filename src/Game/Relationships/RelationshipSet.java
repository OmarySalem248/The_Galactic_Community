package Game.Relationships;

import Game.Colonist.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RelationshipSet {
    private Colonist owner;
    private Map<String, Relationship> relationships; // keyed by other colonist's name

    public RelationshipSet(Colonist owner) {
        this.owner = owner;
        this.relationships = new HashMap<>();
    }

    public void addRelationship(Relationship relationship) {
        relationships.put(relationship.getOtherName(), relationship);
    }

    public void removeRelationshipWith(String otherName) {
        relationships.remove(otherName);
    }

    public Relationship getRelationshipWith(String otherName) {
        return relationships.get(otherName);
    }

    public boolean hasRelationshipWith(String otherName) {
        return relationships.containsKey(otherName);
    }

    public Collection<Relationship> getAllRelationships() {
        return relationships.values();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Relationships of " + owner.getName() + ":\n");
        for (Relationship r : relationships.values()) {
            sb.append(" - ").append(r).append("\n");
        }
        return sb.toString();
    }


    public void adjustRelationship(String otherName, RelationshipType type, int delta) {
        Relationship r = relationships.get(otherName);
        if (r != null) {
            r.adjustValue(type, delta);
        }
    }
}

