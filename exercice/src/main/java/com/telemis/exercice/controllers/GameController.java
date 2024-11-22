package com.telemis.exercice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.services.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    GameService gameService;

    @GetMapping("/rules")
    public Iterable<Rule> getAllRules() {
        return this.gameService.getRules();
    }

    @GetMapping("/games")
    public Iterable<Game> getAllGames() {
        return this.gameService.getGames();
    }
    
    @GetMapping("/game")
    public Game getGame(@RequestParam Long gameId) {
        return this.gameService.getGame(gameId);
    }
    
    @GetMapping("/delete")
    public void deleteGame(@RequestParam Long gameId) {
        this.gameService.deleteGame(gameId);
    }
    
    @GetMapping("/join")
    public Game joinNewGame(@RequestParam Long gameId) {
        return this.gameService.addPlayer(gameId);
    }

    @PostMapping("/new")
    public Game createNewGame(@RequestBody ObjectNode json) {
        return this.gameService.newGame(
            json.get("gameRuleId").asLong(), 
            json.get("name").asText()
        );
    }

    @GetMapping("/roll")
    public Game postMethodName(@RequestParam Long gameId, @RequestParam int pins)  {
        return this.gameService.lancer(gameId, pins);
    }


    
}
