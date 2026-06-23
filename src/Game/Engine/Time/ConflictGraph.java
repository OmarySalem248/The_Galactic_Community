package Game.Engine.Time;

import Game.Engine.Buildings.Building;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Tile;

import java.util.*;

/**
 * ConflictGraph.java
 * Built once per tick before avatar dispatch.
 * Groups colonists that could conflict this tick onto the same thread.
 * O(n) — each avatar is processed once via hash map lookup, not compared against every other.
 */
public class ConflictGraph {

    private final List<List<ColonistAvatar>> groups = new ArrayList<>();
    private final Map<ColonistAvatar, Integer> groupIndex = new HashMap<>();

    // Maps conflict keys (tiles, buildings) to the group that already claimed them
    private final Map<Object, Integer> keyToGroup = new HashMap<>();

    public ConflictGraph(List<ColonistAvatar> avatars) {
        build(avatars);
    }

    private void build(List<ColonistAvatar> avatars) {
        for (ColonistAvatar avatar : avatars) {
            Set<Object> keys = getConflictKeys(avatar);

            // Find if any of this avatar's conflict keys already belong to a group
            int existingGroup = keys.stream()
                    .map(keyToGroup::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(-1);

            int groupIdx;
            if (existingGroup == -1) {
                // No conflicts with any existing group — start a new one
                groupIdx = groups.size();
                groups.add(new ArrayList<>());
            } else {
                groupIdx = existingGroup;
            }

            groups.get(groupIdx).add(avatar);
            groupIndex.put(avatar, groupIdx);

            // Register all this avatar's conflict keys to this group
            // so future avatars with matching keys join the same group
            for (Object key : keys) {
                keyToGroup.put(key, groupIdx);
            }
        }
    }

    /**
     * Returns the set of objects that represent shared resources or interaction
     * targets for this avatar this tick.
     * Add new conflict sources here as the game grows (e.g. social targets, storage buildings).
     */
    private Set<Object> getConflictKeys(ColonistAvatar avatar) {
        Set<Object> keys = new HashSet<>();

        Tile current     = avatar.getCurrentTile();
        Tile destination = avatar.getActionManager().getDestination();
        Building building = avatar.getColonist().getAssignedBuilding();

        if (current != null)  keys.add(current);
        if (destination != null) keys.add(destination);
        if (building != null) keys.add(building);

        return keys;
    }

    public List<List<ColonistAvatar>> getGroups() { return groups; }
}