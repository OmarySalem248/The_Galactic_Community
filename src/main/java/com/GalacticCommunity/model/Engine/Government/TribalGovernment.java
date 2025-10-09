package com.GalacticCommunity.model.Engine.Government;

import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Leader;
import com.GalacticCommunity.model.Engine.Colonist.Profession.TribeLeader;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Unemployed;
import com.GalacticCommunity.model.Engine.Colony;
import com.GalacticCommunity.model.Engine.Buildings.Building;
import com.GalacticCommunity.model.Engine.Colonist.Profession.ProfessionRegistry;

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
    public Colonist getSuccessor(){
        return leadership.getSuccessor();
    }


    public void setSuccessor(Colonist successor){
        this.successor = successor;
    }
}
