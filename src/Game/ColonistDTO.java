package Game;

import Engine.Colonist.Colonist;

public class ColonistDTO {
    public int id;
    public String name;
    public String occupation;
    public int age;
    public int energy;
    public int hp;
    public String assignedBuilding;

    public static ColonistDTO fromColonist(Colonist c) {
        ColonistDTO dto = new ColonistDTO();
        dto.id = c.getId();
        dto.name = c.getName();
        dto.occupation = c.getOccupation();
        dto.age = c.getAge();
        dto.energy = c.getEnergy();
        dto.hp = c.getHealth();
        dto.assignedBuilding = c.getAssignedBuilding() != null ? c.getAssignedBuilding().getName() : "Unassigned";
        return dto;
    }
}
