package Game.Engine.Colonist.Memory;

import Game.Engine.Buildings.BuildingType;
import Game.Engine.Map.Tile;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColonistMemory {

    // Sparse memory — only tiles the colonist has actually seen
    private final Map<Tile, MemoryEntry> memoryMap = new HashMap<>();

    /** Called when a colonist sees a tile — updates or adds memory entry. */
    public void observe(Tile tile, int currentTick) {
        BuildingType type = tile.hasBuilding() ? tile.building.getBType() : null;
        memoryMap.put(tile, new MemoryEntry(type, currentTick));
    }

    /** Returns true if the colonist has ever seen a building of this type. */
    public boolean knowsOf(BuildingType type) {
        return memoryMap.values().stream()
                .anyMatch(e -> e.buildingType() == type);
    }

    /** Returns the most recently seen tile containing a building of the given type. */
    public Optional<Tile> recall(BuildingType type) {
        return memoryMap.entrySet().stream()
                .filter(e -> e.getValue().buildingType() == type)
                .max(Comparator.comparingInt(e -> e.getValue().tickSeen()))
                .map(Map.Entry::getKey);
    }

    /** Returns all tiles the colonist has seen — useful for biased wandering. */
    public java.util.Set<Tile> getExploredTiles() {
        return memoryMap.keySet();
    }

    public boolean hasSeen(Tile tile) {
        return memoryMap.containsKey(tile);
    }
}