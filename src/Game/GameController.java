package Game;

import Engine.Actions.AssignAction;
import Engine.Buildings.Building;
import Engine.Colonist.Colonist;
import Engine.Game;

@RestController
@RequestMapping("/api")
public class GameController {

    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    @GetMapping("/state")
    public GameState getState() {
        return game.toGameState(); // convert game to JSON DTO
    }

    @PostMapping("/feed")
    public boolean feedColonist(@RequestParam String name, @RequestParam int amount) {
        Colonist c = game.getColony().findColonistByName(name);
        if (c != null) return game.getColony().feedColonist(c, amount);
        return false;
    }

    @PostMapping("/assign")
    public boolean assignBuilding(@RequestParam String colonist, @RequestParam String building) {
        Colonist c = game.getColony().findColonistByName(colonist);
        Building b = game.getColony().findBuildingByName(building);
        return game.getColony().performAction(new AssignAction(c, b));
    }

    @PostMapping("/nextTurn")
    public void nextTurn() {
        game.nextTurn();
    }

}
