package Game.Engine.Map.Tiles;



import Game.Engine.Buildings.Building;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.Tiles.Tile;

import java.util.List;

/**
 * MemoryTile.java
 * A colonist's remembered snapshot of a real tile at time of last observation.
 * Mirrors the real tile's building and state at the tick it was observed.
 * Can drift from reality over time — tick saved for future memory decay.
 */
public class MemoryTile extends Tile {

    private Building rememberedBuilding;
    private long lastObservedTick;

    private BuildingSnapshot snapshot;

    public MemoryTile(Tile realTile, long tick) {
        super(realTile.col, realTile.row);
        update(realTile, tick);
    }

    /** Refresh this memory tile from the current real tile state. */
    public void update(Tile realTile, long tick) {


        this.lastObservedTick   = tick;
        // Copy building reference to parent Tile field so existing code still works
        this.building = rememberedBuilding;
    }

    public void setSnap(BuildingSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public long getLastObservedTick() { return lastObservedTick; }
    public Building getRememberedBuilding() { return rememberedBuilding; }

    public BuildingSnapshot getSnap() {
        return snapshot;
    }
}