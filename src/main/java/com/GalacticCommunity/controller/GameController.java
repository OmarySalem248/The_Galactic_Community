package com.GalacticCommunity.controller;





import com.GalacticCommunity.model.Engine.Colonist.Colonist;
import com.GalacticCommunity.model.Engine.Colonist.Profession.Profession;
import com.GalacticCommunity.model.Engine.Game;
import com.GalacticCommunity.model.Engine.Relationships.Relationship;
import com.GalacticCommunity.model.Engine.Relationships.RelationshipType;
import com.GalacticCommunity.model.Engine.Resources;
import com.GalacticCommunity.service.GameService;
import org.springframework.web.bind.annotation.*;


import java.util.List;



import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*") // allow access from web UI
public class GameController {

    private final Game game;

    @Autowired
    public GameController() {
        this.game = new Game();
    }


    @GetMapping("/status")
    public String getStatus() {
        return game.getStatus();
    }

    @GetMapping("/turn")
    public int getTurn() {
        return game.getTurn();
    }

    @GetMapping("/resources")
    public Resources getResources() {
        return game.getColony().getResources();
    }



    @GetMapping("/colonists")
    public List<Map<String, Object>> getColonists() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Colonist c : game.getColony().getColonists()) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("name", c.getName());
            data.put("age", c.getAge());
            data.put("sex", c.getSex());
            data.put("health", c.getHealth());
            data.put("energy", c.getEnergy());
            data.put("occupation", c.getOccupation());
            data.put("profession", c.getProfession() != null ? c.getProfession().toString() : "None");
            data.put("building", c.getAssignedBuilding() != null ? c.getAssignedBuilding().getName() : "Unassigned");
            data.put("status", c.getStatus());
            data.put("pregnant", c.getPregnancy() != null);
            data.put("mono", c.getPersonality() != null ? c.getPersonality().getMono() : null);

            // Optional - children or family data
            data.put("childrenCount", c.getChildren() != null ? c.getChildren().size() : 0);
            data.put("relationships", c.getRelationships());

            list.add(data);
        }
        return list;
    }



    @PostMapping("/feed/{name}")
    public String feedColonist(@PathVariable Colonist c, @RequestParam(defaultValue = "1") int delta) {
        if (c == null) return "Colonist not found";
        if (delta > 0) {
            game.getColony().feedColonist(c, delta);
        } else {
            game.getColony().feedColonist(c, -delta);
        }
        return "OK";
    }

    @PostMapping("/nextTurn")
    public String nextTurn() {
        game.nextTurn();
        return "Turn advanced to " + game.getTurn();
    }

    private List<Map<String, Object>> convertRelationships(List<Relationship> rels) {
        if (rels == null) return List.of();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Relationship r : rels) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("otherName", r.getOtherName());
            map.put("romantic", r.getValue(RelationshipType.ROMANTIC));
            map.put("friendship", r.getValue(RelationshipType.PLATONIC));
            map.put("family", r.getValue(RelationshipType.FAMILIAL));
            map.put("proximity", r.getValue(RelationshipType.PROXIMITY));
            map.put("Admiration", r.getValue(RelationshipType.ADMIRATION));
            map.put("Sexual", r.getValue(RelationshipType.SEXUAL));
            data.add(map);
        }
        return data;
    }
}




