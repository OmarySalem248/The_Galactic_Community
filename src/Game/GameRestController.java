package Game;

@RestController
@RequestMapping("/api/game")
public class GameRestController {

    private final GameController controller;

    public GameRestController() {
        this.controller = new GameController(new Game());
    }

    @PostMapping("/next-turn")
    public Game nextTurn() {
        controller.nextTurn();
        return controller.getGame();
    }

    @PostMapping("/feed-colonist/{name}")
    public Game feedColonist(@PathVariable String name) {
        controller.feedColonist(name, 1);
        return controller.getGame();
    }

    @PostMapping("/assign/{colonist}/{building}")
    public Game assign(@PathVariable String colonist, @PathVariable String building) {
        controller.assignColonistToBuilding(colonist, building);
        return controller.getGame();
    }

    @GetMapping("/state")
    public Game getState() {
        return controller.getGame();
    }
}

