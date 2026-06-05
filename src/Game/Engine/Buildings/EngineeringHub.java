package Game.Engine.Buildings;

import Game.Engine.Colonist.Profession.Engineer;
import Game.Engine.Colonist.Profession.Profession;

public class EngineeringHub extends Building{
    public EngineeringHub() {
        super("Engineering Hub", 100, 100, 1, Engineer.class);
    }
}
