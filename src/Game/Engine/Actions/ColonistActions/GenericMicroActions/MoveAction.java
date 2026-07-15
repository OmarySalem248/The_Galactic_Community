package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Event.ColonistMoveEvent;
import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Map.GameMap;
import Game.Engine.Map.Tiles.Coords;
import Game.Engine.Map.Tiles.MemoryTile;
import Game.Engine.Map.Tiles.Tile;

public class MoveAction extends MicroAction {


    private final Coords destination;
    private GameEventBus eventBus;

    private GameMap memoryMap;

    public MoveAction(ActionManager colonist, Coords destination) {
        super("Move",colonist);
        this.destination = destination;
        this.eventBus = colonistam.getEventBus();
        this.memoryMap = colonistam.getMemoryMap();

    }

    @Override
    public boolean execute() {
        Tile current = colonistam.getCurrentTile();
        if (current == null || destination == null) return false;
        if (current.same(destination)) return false;

        int col = current.col;
        int row = current.row;

        if (col != destination.x()) {
            col += (destination.x() > col) ? 1 : -1;
        } else if (row != destination.y()) {
            row += (destination.y() > row) ? 1 : -1;
        }

        MemoryTile next = (MemoryTile) memoryMap.getTile(col, row);
        if (next == null) return false;
        eventBus.fire(new GameEvent<>(GameEventType.COLONIST_MOVE, new ColonistMoveEvent(colonistam.getAvatar(),next)));
        colonist.modEnergy(-1);

        return true;
    }
}