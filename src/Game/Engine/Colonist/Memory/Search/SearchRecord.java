package Game.Engine.Colonist.Memory.Search;

import Game.Engine.Map.Tile;

/**
 * Records the outcome of a colonist's search attempt.
 * foundAt is populated on success and used as future memory recall.
 * cooldownExpiresTick is only meaningful when result is FAILED/COOLDOWN.
 */
public record SearchRecord(SearchResult result, long cooldownExpiresTick, Tile foundAt) {

    public boolean isOnCooldown(long currentTick) {
        return result == SearchResult.FAILED && currentTick < cooldownExpiresTick;
    }

    public boolean hasLocation() {
        return foundAt != null;
    }
}
