package Game.Engine.Time;



public record GameTime(int minute, int hour, int day, int weekday, int tick) {

    public String display() {
        return String.format("Year %d  Day %d  %02d:%02d", day, hour, minute);
    }
}