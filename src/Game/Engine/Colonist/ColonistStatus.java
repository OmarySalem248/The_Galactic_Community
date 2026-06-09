package Game.Engine.Colonist;

public class ColonistStatus {

    private ColonistAvatar avatar;
    private Colonist colonist;
    private  ActionManager aman;

    private boolean atWork;
    public ColonistStatus(ColonistAvatar avatar){
        this.avatar = avatar;
        this.colonist = avatar.getColonist();
        this.aman = avatar.getActionManager();
    }

    public void update(){
        atWork = colonist.getAssignedBuilding().getCoords().contains(avatar.getCurrentTile());
    }

    public boolean getatWork(){
        return atWork;
    }
}
