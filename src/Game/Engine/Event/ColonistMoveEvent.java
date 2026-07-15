package Game.Engine.Event;

import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Tiles.MemoryTile;

public record ColonistMoveEvent(ColonistAvatar avatar, MemoryTile to) {
}
