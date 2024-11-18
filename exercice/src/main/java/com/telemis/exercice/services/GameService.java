package com.telemis.exercice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Player;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.repositories.GameRepository;
import com.telemis.exercice.repositories.RuleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GameService {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    RuleRepository ruleRepo;

    public Game newGame(Long gameRuleId, String gameName) {
        Rule rule = this.ruleRepo.findById(gameRuleId).orElseThrow();

        Game game = new Game();
        game.setRule(rule);
        game.setName(gameName);

        return this.gameRepo.save(game);
    }

    
    /*** donne le résultat du lancer suivant pour ce joueur
    *   @param quilles donne le nombre de quilles abattues par ce lancer 
    ***/
    public Game lancer(Long gameId, int quilles) {
        Game game = this.gameRepo.findById(gameId).orElseThrow();


        return game;
    }


    public Iterable<Rule> getRules() {
        return this.ruleRepo.findAll();
    }


    public Game addPlayer(Long gameId, String playerName) {
        Game game = this.gameRepo.findById(gameId).orElseThrow();
        List<Player> players = game.getPlayers();
        if(players == null) players = new ArrayList<Player>();
        players.add(new Player(playerName));
        game.setPlayers(new ArrayList<Player>(players));
        return this.gameRepo.save(game);
    }
    
}
