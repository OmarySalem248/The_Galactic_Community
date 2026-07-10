package Game.Engine.Inventory;



import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.BuildingType;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * ColonyInvMap.java
 * Read-only view of all storage buildings in the colony.
 * Used to check project affordability before approval.
 * Holds live references — no update needed, always reflects current state.
 *
 * Production building inventories intentionally excluded —
 * workers must transport to storage before resources count toward projects.
 */
public class ColonyInvMap {

    // Key: building ID, Value: the storage building itself
    private final Map<Integer, Building> storageMap = new HashMap<>();

    public ColonyInvMap() {}

    /** Register a storage building when it's built or placed on the map. */
    public void register(Building building) {
        if (building.getBType() == BuildingType.STORAGE) {
            storageMap.put(building.getId(), building);
        }
    }

    /** Unregister a storage building if it's demolished. */
    public void unregister(Building building) {
        storageMap.remove(building.getId());
    }

    /**
     * Checks if the colony collectively has enough resources for a project.
     * @param required the project's required resources as an Inventory
     * @return an Inventory of items still missing — empty means affordable
     */
    public Inventory checkAffordability(Inventory required) {
        Inventory missing = new Inventory(Float.MAX_VALUE);

        for (ItemStack needed : required.getStacks()) {
            Item item = needed.getItem();
            int totalHave = getTotalAvailable(item.getClass());
            int deficit = needed.getQuantity() - totalHave;
            if (deficit > 0) {
                missing.add(item, deficit);
            }
        }

        return missing;
    }

    public boolean canAfford(Inventory required) {
        return checkAffordability(required).isEmpty();
    }

    /** Total quantity of a specific item class across all storage buildings. */
    public int getTotalAvailable(Class<? extends Item> itemClass) {
        return storageMap.values().stream()
                .flatMap(b -> b.getInv().getByClass(itemClass).stream())
                .mapToInt(ItemStack::getQuantity)
                .sum();
    }

    public Map<Integer, Building> getStorageMap() { return storageMap; }

    // TODO: ambitious scope — index resources by building so lookup goes to
    // the most likely storage unit first rather than iterating all of them.
    // e.g. Map<Class<? extends Item>, List<Integer>> resourceIndex
    // updated when deliveries are deposited into storage.
}