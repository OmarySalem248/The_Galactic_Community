package Game.Engine.Map.Tiles;


import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.Projects.BuildingProject;
import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Event.TileGetNeighboursEvent;
import Game.Engine.Map.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Tile.java
 * Represents a single cell on the map.
 */
public class Tile {
    private BuildingProject buildProject = null;

    private Coords coords;
    public final int col, row;
    public Building building;
    private final List<ColonistAvatar> colonists = new CopyOnWriteArrayList<>();

    public Tile(int col, int row) {
        this.col      = col;
        this.row      = row;
        this.building = null;
        this.coords = new Coords(col, row);


    }


    public Coords getCoords() {
        return coords;
    }

    public boolean sameCoords(Tile other){
        return coords.equals(other.getCoords());
    }
    public boolean same(Coords other){
        return coords.equals(other);
    }

    public boolean hasBuilding() {
        return building != null;
    }

    public void colonistEnter(ColonistAvatar c){
        colonists.add(c);
    }

    public void colonistExit(ColonistAvatar c){
        colonists.remove(c);
    }


    public void placeBuilding(Building building){
        building.setTileCoords(this);
        building.setCoords(this.coords);
        this.building = building;
    }

    @Override
    public String toString() {
        return "Tile[" + col + "," + row + (hasBuilding() ? " " + building : "") + "]";
    }

    public List<ColonistAvatar>getColonists() {
        return colonists;
    }

    public boolean isEmpty() {
        return (!hasBuilding() && getColonists().isEmpty());
    }
    public boolean blocksVision() {
        // Buildings block vision — terrain blocking can be added here later
        return hasBuilding();
    }
    public BuildingProject getBuildProject() { return buildProject; }
    public void setBuildProject(BuildingProject p) { this.buildProject = p; }

    public List<Tile> getNeighbours(GameEventBus eventBus) {

        List<Tile> neighbours = new ArrayList<>();
        eventBus.fire(new GameEvent<>(GameEventType.TILE_NEIGHBOURS,new TileGetNeighboursEvent(this, neighbours)));
        return neighbours;
    }

    public Building getBuilding() {
        return building;
    }


}
