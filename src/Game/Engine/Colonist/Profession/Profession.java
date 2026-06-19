package Game.Engine.Colonist.Profession;


import Game.Engine.Actions.ColonistActions.WorkAction.*;
import Game.Engine.Actions.ColonistActions.WorkAction.WorkAction;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Map.Tile;
import Game.Engine.Time.GameTime;


public abstract class Profession {
    private String name;
    private Class<? extends WorkAction> workAction;
    public  Profession(String name, Class<? extends WorkAction> workAction){
        this.name = name;
        this.workAction = workAction;
    }

    public Class<? extends WorkAction> getWorkAction(){
        return workAction;
    }
    public void work(ActionManager am) {
        try {
            WorkAction action = workAction.getDeclaredConstructor(ActionManager.class).newInstance(am);
            action.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute work action for " + name, e);
        }
    }
    public String getName() {
        return name;
    }


    public boolean isItWorkHours(GameTime time) {
        return (time.hour()) >= 8 && time.hour() <= 17 && time.weekday() >= 1 && time.weekday() <= 5;
    }
}



