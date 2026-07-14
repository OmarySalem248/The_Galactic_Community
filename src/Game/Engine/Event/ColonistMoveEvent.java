package Game.Engine.Event;

import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Tile;

public record ColonistMoveEvent(ColonistAvatar avatar, Tile to) {
}
