package Game.Engine.Event;

import Game.Engine.Colonist.Memory.ColonistMemory;
import Game.Engine.Map.Tile;

import java.util.List;

public record ColonistCastRayEvent(List<Tile> visible, Tile origin, int dx, int dy) {
}
