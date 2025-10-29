package Game.Engine.Buildings;

import Game.Engine.Colonist.Profession.Woodcutter;

public class LumberMill extends Building{
    public LumberMill() {
        super("LumberMill", 3, 3,2, Woodcutter.class);
    }
}
