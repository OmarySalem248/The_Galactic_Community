package Game.Engine.Event;

import Game.Engine.Inventory.Resources;

public record GameEvent<T>(GameEventType type, T data) {}
