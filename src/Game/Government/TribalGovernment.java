package Game.Government;

import Game.Colonist.Colonist;
import Game.Colonist.Profession.Unemployed;
import Game.Colony;
import Game.Government.Government;
import Game.Government.GovernmentType;
import Game.Buildings.Building;
import Game.Colonist.Profession.ProfessionRegistry;

public class TribalGovernment extends Government {

    public TribalGovernment(Colony colony, Colonist leader) {
        super(colony, leader);
    }

    @Override
    public GovernmentType getType() { return GovernmentType.TRIBAL; }

    @Override
    public void assignJobs() {
        int surplus = colony.getNetfoodprod() - colony.getFoodDemand();
        boolean needFarmers = surplus<0;


        for (Colonist c : colony.getColonists()) {
            if (c.getAge() < 16 || c == leader) continue;
            if (c.getAssignedBuilding() != null) continue;

            if (needFarmers) {
                if(Unemployed.class.isInstance(c.getProfession())){
                    c.setProfession(ProfessionRegistry.create("Farmer"));
                }
                assignToBuilding(c, "Farm");
                if(c.getEnergy()<5){
                    c.feedExtra(5);
                }
                surplus += c.getEnergy();
                if(surplus >= 0){
                    break;
                }
            }
        }
    }

    private void assignToBuilding(Colonist c, String buildingType) {
        for (Building b : colony.getBuildings()) {
            if (b.getName().contains(buildingType) && b.hasVacancy()) {
                c.assignBuilding(b);
                return;
            }
        }
    }
}
