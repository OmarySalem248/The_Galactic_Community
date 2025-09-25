package Game.Colonist;
import Game.Buildings.Building;
import Game.Resources;


public abstract class Colonist {
    protected String name;
    protected int energy;
    protected int baseproductivity;
    protected Building assignedBuilding;
    protected int age;
    protected int agemonths;
    protected int hp;
    protected String type;



    public Colonist(String name,String type,int age,int energy,int baseproductivity) {
        this.name = name;
        this.energy = energy;
        this.age = age;
        this.agemonths= 0;
        this.hp =100;
        this.type = type;
        this.baseproductivity = baseproductivity;
    }

    public String getName() { return name; }
    public String getType(){return type;}
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = Math.max(0, Math.min(energy, 100)); }

    public int getProductivity(int usedenergy) {
        return (baseproductivity * usedenergy);
    }
    public boolean isAlive() {
        return hp > 0;
    }
    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }
    public int getFoodConsumption() { return energy; }

    /**
     * Increase energy by assigning extra food
     * Each food unit restores 10 energy (example)
     */
    public void feedExtra(int extraFood) {
        int energyGain = extraFood;
        setEnergy(energy + energyGain);
    }

    public abstract Resources work(int usedenergy);
    public abstract String getOccupation();

    @Override
    public String toString() {
        return name + " (" + getOccupation() + ")";
    }

    public void unassignBuilding() {
        if (this.assignedBuilding != null) {
            this.assignedBuilding.getColonists().remove(this);
            this.assignedBuilding = null;
        }
    }

    public void assignBuilding(Building building) {
        if( building.getColonists().size()<building.getColonlimit()) {
            building.getColonists().add(this);
            this.assignedBuilding = building;
        }
    }

    public Building getAssignedBuilding() {
        return assignedBuilding;
    }

    public int getAge() {
        return age;
    }
    public int getHealth(){
        return hp;
    }
}
