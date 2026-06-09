package Game.Engine.Event;

public record GameEvent<T>(GameEventType type, T data) {}