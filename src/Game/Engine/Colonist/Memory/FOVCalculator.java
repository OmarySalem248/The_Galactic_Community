package Game.Engine.Colonist.Memory;



import Game.Engine.Map.GameMap;
import Game.Engine.Map.Tile;

import java.util.ArrayList;
import java.util.List;

public class FOVCalculator {

    /**
     * Returns all tiles visible from origin within radius.
     * Uses ray casting — stops ray when it hits a vision blocker.
     */
    public static List<Tile> calculate(Tile origin, int radius, GameMap map) {
        List<Tile> visible = new ArrayList<>();
        visible.add(origin);

        // Cast rays to every tile on the perimeter of the radius
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (dx * dx + dy * dy > radius * radius) continue; // circular FOV

                List<Tile> ray = castRay(origin, dx, dy, map);
                visible.addAll(ray);
            }
        }

        return visible;
    }

    /**
     * Casts a ray from origin toward (dx, dy).
     * Stops when it hits a vision blocker — includes the blocking tile but not beyond.
     */
    private static List<Tile> castRay(Tile origin, int dx, int dy, GameMap map) {
        List<Tile> ray = new ArrayList<>();

        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        for (int i = 1; i <= steps; i++) {
            int col = origin.col + Math.round((float) dx * i / steps);
            int row = origin.row + Math.round((float) dy * i / steps);

            Tile tile = map.getTile(col, row);
            if (tile == null) break;

            ray.add(tile);

            if (tile.blocksVision()) break; // include blocker but stop here
        }

        return ray;
    }
}