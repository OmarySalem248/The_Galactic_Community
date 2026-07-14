package Game.Engine.Actions.ColonistActions.GenericMicroActions;

import Game.Engine.Colonist.ActionManager;
import Game.Engine.Event.ColonistMoveEvent;
import Game.Engine.Event.GameEvent;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Event.GameEventType;
import Game.Engine.Map.GameMap;
import Game.Engine.Map.Tile;

public class MoveAction extends MicroAction {


    private final Tile destination;
    private GameEventBus eventBus;

    private GameMap memoryMap;

    public MoveAction(ActionManager colonist, Tile destination) {
        super("Move",colonist);
        this.destination = destination;
        this.eventBus = colonistam.getEventBus();
        this.memoryMap = colonistam.getMemoryMap();

    }

    @Override
    public boolean execute() {
        Tile current = colonistam.getCurrentTile();
        if (current == null || destination == null) return false;
        if (current == destination) return false;

        int col = current.col;
        int row = current.row;

        if (col != destination.col) {
            col += (destination.col > col) ? 1 : -1;
        } else if (row != destination.row) {
            row += (destination.row > row) ? 1 : -1;
        }

        Tile next = memoryMap.getTile(col, row);
        if (next == null) return false;
        eventBus.fire(new GameEvent<>(GameEventType.COLONIST_MOVE, new ColonistMoveEvent(colonistam.getAvatar(),next)));
        colonist.modEnergy(-1);

        return true;
    }
}