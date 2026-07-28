package Game.Engine.Map.Tiles;

import Game.Engine.Buildings.BuildingType;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;

import java.util.List;

public class BuildingSnapshot {

    private final String name;
    private final BuildingType type;
    private final int id;
    private final Coords coords;
    private final long firstObservedTick;

    private Inventory inventory;        // null until directly checked
    private long inventoryObservedTick; // tick when inventory was last checked

    public BuildingSnapshot(String name, BuildingType type, int id, Coords coords, long tick) {
        this.name              = name;
        this.type              = type;
        this.id                = id;
        this.coords            = coords;
        this.firstObservedTick = tick;
        this.inventory         = null;
        this.inventoryObservedTick = -1;
    }

    /**
     * Called when the colonist directly checks this building's inventory
     * via a COLONIST_OBSERVE event — snapshots the inventory at this tick.
     */
    public void updateInventory(Inventory realInventory, long tick) {
        // Deep copy so snapshot doesn't reference the live inventory
        this.inventory             = realInventory.snapshot();
        this.inventoryObservedTick = tick;
    }

    public boolean hasInventoryData()       { return inventory != null; }

    public String getName()                 { return name; }
    public BuildingType type()              { return type; }
    public int id()                         { return id; }
    public Coords coords()                  { return coords; }
    public long getFirstObservedTick()      { return firstObservedTick; }
    public Inventory getInventory()         { return inventory; }
    public long getInventoryObservedTick()  { return inventoryObservedTick; }


}