package Game.Engine.Map.Tiles;

public record Coords(int x, int y) {



    public boolean equals(Coords other) {
        return (x == other.x && y == other.y);
    }
}
