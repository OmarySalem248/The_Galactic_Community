package Game.Engine.Colonist;

import Game.Engine.Time.GameTime;

public class ColonistStatus {

    private ColonistAvatar avatar;
    private Colonist colonist;
    private  ActionManager aman;

    private boolean atWork;

    private boolean shouldWork;
    public ColonistStatus(ColonistAvatar avatar){
        this.avatar = avatar;
        this.colonist = avatar.getColonist();
        this.aman = avatar.getActionManager();
    }

    public void update(GameTime time){
        atWork = colonist.getAssignedBuilding().getCoords().contains(avatar.getCurrentTile());
        shouldWork =  (time.hour() >= 8 && time.hour() <= 17)&&(time.weekday() >=1 && time.weekday() <= 5);
    }

    public boolean getatWork(){
        return atWork;
    }



    public boolean getshouldWork(){
        return shouldWork;
    }
}
