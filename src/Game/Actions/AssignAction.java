package Game.Actions;

import Game.Buildings.Building;
import Game.Colonist.Colonist;
import Game.Colony;

public class AssignAction extends Action{
    private Colonist colonist;
    private Building building;

    public AssignAction(Colonist colonist, Building building) {
        super("Assign " + colonist.getName() + " to " + (building == null ? "None" : building.getName()));
        this.colonist = colonist;
        this.building = building;
    }

    @Override
    public boolean execute(Colony colony) {
        // Unassign if null
        if (building == null) {
            colonist.unassignBuilding();
            return true;
        }

        // Only allow assignment if occupation matches
        boolean valid = false;
        if (building.getCompatible().isInstance(colonist.getProfession())) valid = true;
        if (building.getColonists().size()>=building.getColonlimit()) valid = false;
        if (valid) {
            colonist.unassignBuilding(); // remove from previous
            colonist.assignBuilding(building);
            return true;
        }
        return false;
    }
}
