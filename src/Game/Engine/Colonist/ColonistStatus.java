package Game.Engine.Colonist;

import Game.Engine.Time.GameTime;

public class ColonistStatus {


    private ColonistAvatar avatar;
    private Colonist colonist;

    private Boolean isSearching;
    private  ActionManager aman;

    private boolean atWork;

    private boolean shouldWork;

    private boolean workToDo;

    private boolean atHome;
    public ColonistStatus(ColonistAvatar avatar){
        this.avatar = avatar;
        this.colonist = avatar.getColonist();
        this.aman = avatar.getActionManager();
        this.isSearching = false;
    }

    public void update(GameTime time){

        atWork = colonist.getAssignedBuilding().getCoords().contains(avatar.getCurrentTile());
        shouldWork =  colonist.getProfession().isItWorkHours(time);
        atHome = colonist.getDwelling().getCoords().contains(avatar.getCurrentTile());
    }

    public boolean getatWork(){
        return atWork;
    }

    public boolean getatHome(){
        return atHome;
    };

    public boolean getshouldWork(){
        return shouldWork;
    }

    public boolean getisSearching(){
        return isSearching;
    }


    public void setIsSearching(Boolean is){
        isSearching = is;
    }
}
