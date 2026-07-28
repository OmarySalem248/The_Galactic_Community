package Game.Engine.Event;

import Game.Engine.Map.MemoryMap;
import Game.Engine.Map.Tiles.Tile;

import java.util.List;

public record ColonistCastRayEvent(List<Tile> visible, Tile origin, int dx, int dy, MemoryMap memoryMap) {
}
