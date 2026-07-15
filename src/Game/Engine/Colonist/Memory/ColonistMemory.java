package Game.Engine.Colonist.Memory;

import Game.Engine.Colonist.Memory.Search.SearchRecord;
import Game.Engine.Colonist.Memory.Search.SearchResult;
import Game.Engine.Event.GameEventBus;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Map.MemoryMap;
import Game.Engine.Map.Tiles.Coords;
import Game.Engine.Map.Tiles.MemoryTile;
import Game.Engine.Map.Tiles.Tile;
import Game.Engine.Map.GameMap;
import Game.Engine.Time.GameTime;

import java.util.*;

public class ColonistMemory {



    public ColonistMemory(MemoryMap memoryMap, GameEventBus eventbus){

        this.memoryMap = memoryMap;
        this.eventBus = eventbus;
    }

    private  GameEventBus eventBus;
    private MemoryMap memoryMap;
    private final Map<Class<? extends Item>, SearchRecord> classSearchHistory = new HashMap<>();
    private final Map<ItemType, SearchRecord>              typeSearchHistory  = new HashMap<>();

    private static final long SEARCH_COOLDOWN_TICKS = 360; // 6 hours

    private GameTime mentalTime = new GameTime(0,0,0,0,0);
    private HashMap<TodoType, ArrayList<ToDo>> todoMap = new HashMap<>();
    public void updateTime(GameTime time){
        mentalTime = time;
    }

    /** Called when a colonist sees a tile — updates or adds memory entry. */




    public void  addToDo(ToDo todo){

        TodoType type = todo.getType();
        if(todoMap.containsKey(type)){

            todoMap.get(type).add(todo);
        }
        else {

            ArrayList<ToDo> newArray = new ArrayList<>();
            newArray.add(todo);
            todoMap.put(type, newArray);
        }
    }

    public ToDo anyWorkToDo(){
        if(!todoMap.containsKey(TodoType.WORK)){
            return null;
        }
        else{
            ArrayList<ToDo> todoList = todoMap.get(TodoType.WORK);
            for(ToDo todo: todoList){
                if(todo.getTime().lessThan(mentalTime)){
                    System.out.println("gotta do that");
                    todoList.remove(todo);
                    return todo;

                }
            }
        }
        return null;
    }
    public MemoryTile wanderUnexplored(MemoryTile current) {
        Random rand = new Random();
        List<Tile> neighbours = current.getNeighbours(eventBus);
        List<Tile> unexplored = neighbours.stream()
                .filter(n -> !hasSeen(n)).toList();
        List<Tile> candidates = unexplored.isEmpty() ? neighbours : unexplored;
        if (candidates.isEmpty()) return current;
        return (MemoryTile) candidates.get(rand.nextInt(candidates.size()));
    }

    public GameTime setTime(GameTime time, int minutes){
        return new GameTime(time.minute() + minutes% 60, time.hour() +Math.floorDiv(minutes,60),
                time.day() +Math.floorDiv(minutes,1440),(time.day() +Math.floorDiv(minutes,1440)%7),
                time.tick() + minutes);
    }
    public void recordClassSearch(Class<? extends Item> itemClass, SearchResult result, long currentTick, Tile foundAt) {
        long cooldown = result == SearchResult.FAILED ? currentTick + SEARCH_COOLDOWN_TICKS : 0;
        classSearchHistory.put(itemClass, new SearchRecord(result, cooldown, foundAt));
    }

    public SearchRecord getClassSearchRecord(Class<? extends Item> itemClass) {
        return classSearchHistory.get(itemClass);
    }

    public boolean isClassOnCooldown(Class<? extends Item> itemClass, long currentTick) {
        SearchRecord rec = classSearchHistory.get(itemClass);
        return rec != null && rec.isOnCooldown(currentTick);
    }

    public Tile recallClassLocation(Class<? extends Item> itemClass) {
        SearchRecord rec = classSearchHistory.get(itemClass);
        return (rec != null && rec.hasLocation()) ? rec.foundAt() : null;
    }

// ---- Type-based search history ----

    public void recordTypeSearch(ItemType type, SearchResult result, long currentTick, Tile foundAt) {
        long cooldown = result == SearchResult.FAILED ? currentTick + SEARCH_COOLDOWN_TICKS : 0;
        typeSearchHistory.put(type, new SearchRecord(result, cooldown, foundAt));
    }

    public SearchRecord getTypeSearchRecord(ItemType type) {
        return typeSearchHistory.get(type);
    }

    public boolean isTypeOnCooldown(ItemType type, long currentTick) {
        SearchRecord rec = typeSearchHistory.get(type);
        return rec != null && rec.isOnCooldown(currentTick);
    }

    public Tile recallTypeLocation(ItemType type) {
        SearchRecord rec = typeSearchHistory.get(type);
        return (rec != null && rec.hasLocation()) ? rec.foundAt() : null;
    }



    /** Returns all tiles the colonist has seen — useful for biased wandering. */

    public boolean hasSeen(Tile tile) {
        return !memoryMap.isFog(tile.col,tile.row);
    }

    public MemoryMap getMemoryMap() {
        return memoryMap;
    }

    public void observe(Coords coords) {
        MemoryTile current = (MemoryTile) memoryMap.getTileFromCoords(coords);
        FOVCalculator.calculate(current, 12,  eventBus);
    }
}