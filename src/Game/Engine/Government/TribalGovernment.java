package Game.Engine.Government;

import Game.Engine.Buildings.Building;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.Profession.*;
import Game.Engine.Colony;

public class TribalGovernment extends Government {

    private Colonist successor;

    public TribalGovernment(Colony colony, Colonist leader) {

        super(colony, leader);
        leadership = (Leader) leader.getProfession();
    }


    @Override
    public GovernmentType getType() { return GovernmentType.TRIBAL; }

    @Override
    public boolean setLeader(Colony colony) {
        if(leadership.getSuccessor() != null) {
            leader = leadership.getSuccessor() ;
            leader.setProfession(new TribeLeader());
            return true;
        }
        return false;
    }

    @Override
    public void assignJobs() {
        int surplus = colony.getNetfoodprod() - colony.getFoodDemand();
        boolean needFarmers = surplus<0;


        for (Colonist c : colony.getColonists()) {
            if (c.getAge() < 16 || c == leader) continue;
            if (c.getAssignedBuilding() != null) continue;

            if (needFarmers) {
                Building vac = findVac("Farm");
                if(vac != null){
                    c.setProfession(new Farmer());
                    assignToBuilding(c,vac);
                }
            }
        }
    }

    private Building findVac(String buildingType){
        for (Building b : colony.getBuildings()) {
            if (b.getName().contains(buildingType) && b.hasVacancy()) {
                return b;
            }
        }
        return null;
    }

    private void assignToBuilding(Colonist c, Building b) {
        c.assignBuilding(b);
        if(c.getEnergy()<5){
            c.feedExtra(5);
        }
    }
    public Colonist getSuccessor(){
        return leadership.getSuccessor();
    }


    public void setSuccessor(Colonist successor){
        this.successor = successor;
    }
}
