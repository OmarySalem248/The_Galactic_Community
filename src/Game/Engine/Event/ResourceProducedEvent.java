package Game.Engine.Event;

import Game.Engine.Inventory.Resources;

public record ResourceProducedEvent(Resources produced, String sourceName) {}
