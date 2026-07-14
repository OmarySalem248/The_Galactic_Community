package Game.Engine.Colonist.Memory;

import Game.Engine.Buildings.BuildingType;
import Game.Engine.Colonist.Memory.Search.SearchRecord;
import Game.Engine.Colonist.Memory.Search.SearchResult;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Map.Tile;
import Game.Engine.Map.GameMap;
import Game.Engine.Time.GameTime;

import java.util.*;

public class ColonistMemory {

    // Sparse memory — only tiles the colonist has actually seen
    private final Map<Tile, MemoryEntry> memoryMap = new HashMap<>();
    private final Map<Class<? extends Item>, SearchRecord> classSearchHistory = new HashMap<>();
    private final Map<ItemType, SearchRecord>              typeSearchHistory  = new HashMap<>();

    private static final long SEARCH_COOLDOWN_TICKS = 360; // 6 hours

    private GameTime mentalTime = new GameTime(0,0,0,0,0);
    private HashMap<TodoType, ArrayList<ToDo>> todoMap = new HashMap<>();
    public void updateTime(GameTime time){
        mentalTime = time;
    }

    /** Called when a colonist sees a tile — updates or adds memory entry. */
    public void observe(Tile tile) {
        BuildingType type = tile.hasBuilding() ? tile.building.getBType() : null;
        memoryMap.put(tile, new MemoryEntry(type, mentalTime.tick()));

    }

    /** Returns true if the colonist has ever seen a building of this type. */
    public boolean knowsOf(BuildingType type) {
        return memoryMap.values().stream()
                .anyMatch(e -> e.buildingType() == type);
    }

    /** Returns the most recently seen tile containing a building of the given type. */
    public Optional<Tile> recall(BuildingType type) {
        return memoryMap.entrySet().stream()
                .filter(e -> e.getValue().buildingType() == type)
                .max(Comparator.comparingInt(e -> e.getValue().tickSeen()))
                .map(Map.Entry::getKey);
    }

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
    public Tile wanderUnexplored(Tile current, GameMap map) {
        Random rand = new Random();
        List<Tile> neighbours = current.getNeighbours(map);
        List<Tile> unexplored = neighbours.stream()
                .filter(n -> !hasSeen(n)).toList();
        List<Tile> candidates = unexplored.isEmpty() ? neighbours : unexplored;
        if (candidates.isEmpty()) return current;
        return candidates.get(rand.nextInt(candidates.size()));
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
    public java.util.Set<Tile> getExploredTiles() {
        return memoryMap.keySet();
    }

    public boolean hasSeen(Tile tile) {
        return memoryMap.containsKey(tile);
    }

    public GameMap getMemoryMap() {
        return memoryMap;
    }
}