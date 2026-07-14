package Game.Engine.Event;


import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Tile;

/**
 * Fired when a colonist wants to observe their immediate 3x3 surroundings.
 * GameMap populates the result and calls back via ActionManager.receiveImmediate().
 */
public record ColonistObserveEvent(ColonistAvatar avatar, ActionManager actionManager, Tile origin) {}