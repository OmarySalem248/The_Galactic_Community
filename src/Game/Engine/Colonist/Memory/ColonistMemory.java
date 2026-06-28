package Game.Engine.Colonist.Memory;

import Game.Engine.Buildings.BuildingType;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;

import java.util.*;

public class ColonistMemory {

    // Sparse memory — only tiles the colonist has actually seen
    private final Map<Tile, MemoryEntry> memoryMap = new HashMap<>();

    private GameTime mentalTime = new GameTime(0,0,0,0,0);
    private HashMap<TodoType, ArrayList<ToDo>> todoMap = new HashMap<>();


    /** Called when a colonist sees a tile — updates or adds memory entry. */
    public void observe(Tile tile, GameTime time) {
        BuildingType type = tile.hasBuilding() ? tile.building.getBType() : null;
        memoryMap.put(tile, new MemoryEntry(type, time.tick()));
        mentalTime = time;
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
        System.out.println("begin");
        TodoType type = todo.getType();
        if(todoMap.containsKey(type)){
            System.out.println("1");
            todoMap.get(type).add(todo);
        }
        else {
            System.out.println("2");
            ArrayList<ToDo> newArray = new ArrayList<>();
            newArray.add(todo);
            todoMap.put(type, newArray);
        }
        System.out.println("end");

    }

    public Boolean anyWorkToDo(){
        if(!todoMap.containsKey(TodoType.WORK)){
            return false;
        }
        else{
            ArrayList<ToDo> todoList = todoMap.get(TodoType.WORK);
            for(ToDo todo: todoList){
                if(todo.getTime().lessThan(mentalTime)){
                    System.out.println("gotta do that");
                    todoList.remove(todo);
                    return true;

                }
            }
        }
        return false;
    }

    public GameTime setTime(GameTime time, int minutes){
        return new GameTime(time.minute() + minutes% 60, time.hour() +Math.floorDiv(minutes,60),
                time.day() +Math.floorDiv(minutes,1440),(time.day() +Math.floorDiv(minutes,1440)%7),
                time.tick() + minutes);
    }



    /** Returns all tiles the colonist has seen — useful for biased wandering. */
    public java.util.Set<Tile> getExploredTiles() {
        return memoryMap.keySet();
    }

    public boolean hasSeen(Tile tile) {
        return memoryMap.containsKey(tile);
    }
}