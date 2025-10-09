package Game;

import com.GalacticCommunity.model.Engine.Resources;

import java.util.List;

public class GameState {
    public List<ColonistDTO> colonists;
    public List<BuildingDTO> buildings;
    public Resources resources;
    public int turn;
}

