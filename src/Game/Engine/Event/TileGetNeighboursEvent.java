package Game.Engine.Event;

import Game.Engine.Map.Tiles.Tile;

import java.util.List;

public record TileGetNeighboursEvent(Tile origin, List<Tile> neighbours){}