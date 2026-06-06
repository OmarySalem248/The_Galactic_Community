package Game.Engine.Actions.ColonistActions;

import Game.Engine.Colonist.ColonistAvatar;
import Game.Engine.Map.Map;
import Game.Engine.Map.Tile;

public class MoveAction extends ColonistAction {

    private final ColonistAvatar avatar;
    private final Tile destination;
    private final Map map;

    public MoveAction(ColonistAvatar avatar, Tile destination, Map map) {
        super("Move");
        this.avatar      = avatar;
        this.destination = destination;
        this.map         = map;
    }

    @Override
    public boolean execute() {
        Tile current = avatar.getCurrentTile();
        if (current == null || destination == null) return false;
        if (current == destination) return false;

        int col = current.col;
        int row = current.row;

        if (col != destination.col) {
            col += (destination.col > col) ? 1 : -1;
        } else if (row != destination.row) {
            row += (destination.row > row) ? 1 : -1;
        }

        Tile next = map.getTile(col, row);
        if (next == null) return false;

        avatar.setCurrentTile(next);
        return true;
    }
}