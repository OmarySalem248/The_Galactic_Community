package Game.Modes;

import Game.Engine.Buildings.Building;
import Game.Engine.Buildings.EngineeringHub;

/**
 * Tracks global build mode state.
 * Held on Game so any system can query whether build mode is active.
 */
public class BuildMode {

    private boolean active = false;
    private EngineeringHub activeHub = null;

    public void enter(EngineeringHub hub) {
        this.active    = true;
        this.activeHub = hub;
    }

    public void exit() {
        this.active    = false;
        this.activeHub = null;
    }

    public boolean isActive()            { return active; }
    public EngineeringHub getActiveHub() { return activeHub; }
}
