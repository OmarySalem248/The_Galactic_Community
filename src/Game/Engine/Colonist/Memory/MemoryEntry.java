package Game.Engine.Colonist.Memory;



import Game.Engine.Buildings.BuildingType;

/**
 * What a colonist remembers about a tile they have seen.
 * tickSeen allows memory to age — useful for future memory decay.
 */
public record MemoryEntry(BuildingType buildingType, int tickSeen) {

    public boolean hasBuilding() {
        return buildingType != null;
    }
}