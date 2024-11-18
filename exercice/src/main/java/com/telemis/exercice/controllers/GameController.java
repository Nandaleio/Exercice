package com.telemis.exercice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.models.Scoreboard;
import com.telemis.exercice.services.FrameService;
import com.telemis.exercice.services.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    FrameService frameService;

    @GetMapping("/rules")
    public Iterable<Rule> getAllRules() {
        return this.gameService.getRules();
    }

    @GetMapping("/games")
    public Iterable<Game> getAllGames() {
        return this.gameService.getGames();
    }
    
    @PostMapping("/join")
    public Game joinNewGame(@RequestBody ObjectNode json) {
        return this.gameService.addPlayer(
            json.get("gameId").asLong(), 
            json.get("name").asText()
        );
    }

    @PostMapping("/new")
    public Game createNewGame(@RequestBody ObjectNode json) {
        return this.gameService.newGame(
            json.get("gameRuleId").asLong(), 
            json.get("name").asText()
        );
    }

    @PostMapping("/throw")
    public Scoreboard postMethodName(@RequestBody ObjectNode json) { //@RequestBody int pins, @RequestBody Long gameId) {
        
        this.frameService.createNextFrame(null, null, 0);
        return null;
    }
    

    
}
