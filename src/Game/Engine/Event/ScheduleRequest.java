package Game.Engine.Event;

import Game.Engine.Time.Tickable;

public record ScheduleRequest(Tickable target, long delay) {}