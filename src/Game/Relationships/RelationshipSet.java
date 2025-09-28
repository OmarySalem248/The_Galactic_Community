package Game.Relationships;

import Game.Colonist.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RelationshipSet {
    private Colonist owner;
    private Map<String, Relationship> relationships;

    public RelationshipSet(Colonist owner) {
        this.owner = owner;
        this.relationships = new HashMap<>();
    }
    public Set<Map.Entry<String, Relationship>> entrySet() {
        return relationships.entrySet();
    }
    public void addRelationship(Relationship relationship) {
        relationships.put(relationship.getOtherName(), relationship);
    }

    public void removeRelationshipWith(String otherName) {
        relationships.remove(otherName);
    }
    public Map<String, Relationship> getRelationships(){
        return relationships;
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

    public Relationship getOrCreate(Colonist other) {
        if (this.hasRelationshipWith(other.getName())){
            return  this.getRelationshipWith(other.getName());
        }
        addRelationship(new Relationship(owner,other,"Neutral"));
        return this.getRelationshipWith(other.getName());
    }



    public Set<String> getKeys() {
        return relationships.keySet();
    }

    public Relationship get(String otherName) {
        return relationships.get(otherName);
    }
}

