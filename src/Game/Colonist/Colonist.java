package Game.Colonist;

import Game.Buildings.Building;
import Game.Relationships.RelationshipSet;
import Game.Resources;


import java.util.ArrayList;

public class Colonist {
    private String name;
    private int energy;
    private int baseProductivity;
    private Building assignedBuilding;
    private int age;
    private int ageMonths;
    private int hp;
    private Colonist bioFather;
    private Colonist bioMother;
    private ArrayList<Colonist> children;
    private String status;
    private RelationshipSet relationships;

    // ----- Composition: profession object -----
    private Profession profession;

    public Colonist(String name, Profession profession, int age, int energy, int baseProductivity) {
        this.name = name;
        this.profession = profession;
        this.age = age;
        this.ageMonths = 0;
        this.energy = energy;
        this.baseProductivity = baseProductivity;
        this.hp = 100;
        this.children = new ArrayList<>();
        this.relationships = new RelationshipSet(this);
        this.status = "Where am I?";
    }

    // ----- Basic getters and setters -----
    public String getName() { return name; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = Math.max(0, Math.min(energy, 100)); }
    public int getAge() { return age; }
    public int getAgeMonths() { return ageMonths; }
    public int getHealth() { return hp; }
    public Profession getProfession;
    public RelationshipSet getRelationships() { return relationships; }

    public void setParents(Colonist mom, Colonist dad) {
        this.bioMother = mom;
        this.bioFather = dad;
    }

    public boolean isAlive() { return hp > 0; }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    public int getFoodConsumption() {
        return energy; // or you can tweak this later
    }

    public void feedExtra(int extraFood) {
        int energyGain = extraFood; // adjust formula if needed
        setEnergy(energy + energyGain);
    }

    public Building getAssignedBuilding() { return assignedBuilding; }

    public void assignBuilding(Building building) {
        if (building.getColonists().size() < building.getColonlimit()) {
            building.getColonists().add(this);
            this.assignedBuilding = building;
        }
    }

    public void unassignBuilding() {
        if (assignedBuilding != null) {
            assignedBuilding.getColonists().remove(this);
            assignedBuilding = null;
        }
    }

    public void age() {
        ageMonths += 1;
        if (ageMonths == 12) {
            ageMonths = 0;
            age += 1;
        }
    }


    public Resources work(int usedEnergy) {
        if (profession == null) return new Resources(0, 0, 0);
        return profession.work(this, usedEnergy);
    }

    public String getOccupation() {
        return profession != null ? profession.getName() : "Unassigned";
    }

    public void changeProfession(Profession newProfession) {
        this.profession = newProfession;
    }

    // ----- Children and family -----
    public ArrayList<Colonist> getChildren() { return children; }
    public Colonist getBioFather() { return bioFather; }
    public Colonist getBioMother() { return bioMother; }

    // ----- Status -----
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return name + " (" + getOccupation() + ")";
    }

    public Profession getProfession() {
        return  profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }
}

