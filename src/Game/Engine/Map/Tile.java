package Game.Engine.Map;


import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.Projects.BuildingProject;
import Game.Engine.Colonist.ColonistAvatar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Tile.java
 * Represents a single cell on the map.
 */
public class Tile {
    private BuildingProject buildProject = null;


    public final int col, row;
    public Building building;
    private final List<ColonistAvatar> colonists = new CopyOnWriteArrayList<>();

    public Tile(int col, int row) {
        this.col      = col;
        this.row      = row;
        this.building = null;


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
        building.setcoords(this);
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

    public List<Tile> getNeighbours(GameMap map) {
        List<Tile> neighbours = new ArrayList<>();
        int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] offset : offsets) {
            Tile t = map.getTile(col + offset[0], row + offset[1]);
            if (t != null) neighbours.add(t);
        }
        return neighbours;
    }

    public Building getBuilding() {
        return building;
    }


}
