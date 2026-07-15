package Game.Engine.Colonist.Memory;



import Game.Engine.Event.ColonistCastRayEvent;
import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Map.Tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class FOVCalculator {

    /**
     * Returns all tiles visible from origin within radius.
     * Uses ray casting — stops ray when it hits a vision blocker.
     */
    public static List<Tile> calculate(Tile origin, int radius, GameEventBus eventBus) {
        List<Tile> visible = new ArrayList<>();
        visible.add(origin);

        // Cast rays to every tile on the perimeter of the radius
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (dx * dx + dy * dy > radius * radius) continue; // circular FOV


                eventBus.fire(new GameEvent<>(GameEventType.COLONIST_CASTRAY, new ColonistCastRayEvent(visible,origin,dx,dy)));

            }
        }

        return visible;
    }

    /**
     * Casts a ray from origin toward (dx, dy).
     * Stops when it hits a vision blocker — includes the blocking tile but not beyond.
     */

}