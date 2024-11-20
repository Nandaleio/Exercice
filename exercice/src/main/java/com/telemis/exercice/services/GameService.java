package com.telemis.exercice.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.telemis.exercice.models.Frame;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.models.UserPlayer;
import com.telemis.exercice.repositories.FrameRepository;
import com.telemis.exercice.repositories.GameRepository;
import com.telemis.exercice.repositories.RuleRepository;
import com.telemis.exercice.repositories.UserRepository;
import com.telemis.exercice.utils.GameLogics;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GameService {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    RuleRepository ruleRepo;

    @Autowired
    FrameRepository frameRepo;
    
	@Autowired
    private UserRepository userRepository;

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
    public Game lancer(Long gameId, int pins) {
        Game game = this.gameRepo.findById(gameId).orElseThrow();

        Rule rule = game.getRule();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPlayer currentUser = this.userRepository.findByUsername(auth.getName()).orElseThrow();

        if(!game.getPlayers().contains(currentUser)) throw new IllegalStateException("Player not in the game");

        // get last frame
        Frame currentFrame = game.getPlayerCurrentFrame(currentUser);
        if (currentFrame == null) {
            throw new IllegalStateException("No current frame found or game is complete.");
        }

        if (currentFrame.getFrameNumber() == rule.getMaxFrames()) {
            GameLogics.handleFinalFrameRoll(rule, currentFrame, pins);
        } else {
            GameLogics.handleRegularFrameRoll(rule, currentFrame, pins);
        }

        // Update frame score
        GameLogics.updateFrameScore(currentFrame);

        // Return updated frame
        this.frameRepo.save(currentFrame);

        return game;
    }

    public Iterable<Rule> getRules() {
        return this.ruleRepo.findAll();
    }

    public Iterable<Game> getGames() {
        return this.gameRepo.findAll();
    }

    public Game getGame(Long gameId) {
        return this.gameRepo.findById(gameId).orElseThrow();
    }

    public Game addPlayer(Long gameId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPlayer currentUser = this.userRepository.findByUsername(auth.getName()).orElseThrow();

        Game game = this.gameRepo.findById(gameId).orElseThrow();
        List<UserPlayer> players = game.getPlayers();
        if(players == null) players = new ArrayList<UserPlayer>();

        if(players.contains(currentUser)) { // user already in game
            return game;
        }

        players.add(currentUser);
        game.setPlayers(new ArrayList<UserPlayer>(players));
        return this.gameRepo.save(game);
    }


    public void deleteGame(Long gameId) {
        this.gameRepo.deleteById(gameId);
    }
    
}
