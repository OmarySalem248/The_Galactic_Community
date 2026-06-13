package Game.Engine.Actions.Interactions;

import Game.Engine.Colonist.Colonist;
import Game.Engine.Relationships.Relationship;
import Game.Engine.Relationships.RelationshipSet;

public abstract class InteractAction {

    protected final Colonist c1;
    protected final Colonist c2;
    protected final Relationship rel1;
    protected final Relationship rel2;
    protected final int duration; // in ticks
    protected int ticksElapsed = 0;

    public InteractAction(Colonist c1, Colonist c2, int duration) {
        this.c1       = c1;
        this.c2       = c2;
        this.duration = duration;

        // Look up or create relationships internally
        this.rel1 = getOrCreate(c1, c2);
        this.rel2 = getOrCreate(c2, c1);
    }

    /** Called each tick — returns true when complete. */
    public boolean tick() {
        onTick();
        ticksElapsed++;
        if (ticksElapsed >= duration) {
            onComplete();
            return true;
        }
        return false;
    }

    /** Partial effects applied each tick. */
    protected abstract void onTick();

    /** Final resolution when duration is reached. */
    protected abstract void onComplete();

    private Relationship getOrCreate(Colonist owner, Colonist other) {
        RelationshipSet rels = owner.getRelationships();
        Relationship rel = rels.get(other.getName());
        if (rel == null) {
            rel = new Relationship(owner, other, "None");
            rels.addRelationship(rel);
        }
        return rel;
    }
}