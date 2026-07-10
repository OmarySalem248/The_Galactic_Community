package Game.Engine.Colonist.Memory.Search;

/**
 * Abstract base for a resource search query carried by a SearchTile.
 */
public abstract class Search {

    /** Check if a given item matches what this search is looking for. */
    public abstract boolean matches(Game.Engine.Inventory.Items.Item item);

    /** Human-readable description for status display. */
    public abstract String describe();
}
