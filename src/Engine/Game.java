package Engine;

import java.util.stream.Collectors;
import Engine.Colonist.*;
import Engine.Colony;
import Engine.Resources;

public class Game {
    private int turn;
    private Colony colony;

    private String status;

    public Game() {
        this.turn = 1;
        this.colony = new Colony(7, new Resources(20, 15, 10));
        this.status = this.colony.getStatus();
    }

    public int getTurn() {
        return turn;
    }

    public String getStatus(){
        return this.colony.getStatus();
    }

    public Colony getColony() {
        return colony;
    }

    public void nextTurn() {
        turn++;
        colony.consumeAndProduce();
        colony.ageColonists();
        colony.developRelationships();
        if (this.turn ==12){
            colony.getLeadership().setLeadership(colony);
        }
    }
    public GameState toGameState() {
        GameState state = new GameState();
        state.turn = this.getTurn();
        state.resources = this.getColony().getResources();

        state.colonists = this.getColony().getColonists().stream().map(c -> {
            ColonistDTO dto = new ColonistDTO();
            dto.name = c.getName();
            dto.occupation = c.getOccupation();
            dto.profession = c.getProfession().getName();
            dto.energy = c.getEnergy();
            dto.health = c.getHealth();
            dto.age = c.getAge();
            dto.building = c.getAssignedBuilding() != null ? c.getAssignedBuilding().getName() : null;
            dto.isLeader = this.getColony().getLeadership().getCurrentLeader() == c;
            return dto;
        }).collect(Collectors.toList());

        state.buildings = this.getColony().getBuildings().stream().map(b -> {
            BuildingDTO dto = new BuildingDTO();
            dto.name = b.getName();
            dto.colonistLimit = b.getColonlimit();
            dto.assignedColonists = b.getColonists().stream().map(Colonist::getName).collect(Collectors.toList());
            return dto;
        }).collect(Collectors.toList());

        return state;
    }

}
