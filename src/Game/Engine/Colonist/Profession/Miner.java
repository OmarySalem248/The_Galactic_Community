package Game.Engine.Colonist.Profession;

import Game.Engine.Actions.ColonistActions.WorkAction.MineAction;

public class Miner extends Profession {
    public Miner() {
        super("Miner", MineAction.class);
    }


    public String getName() { return "Miner"; }
}
