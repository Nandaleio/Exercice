package com.telemis.exercice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.telemis.exercice.exceptions.GameNotFoundException;
import com.telemis.exercice.exceptions.NoMoreFrameAllowedException;
import com.telemis.exercice.exceptions.NoPlayerInGameException;
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
    
    private Random rand = new Random();

    @Autowired
    GameRepository gameRepo;

    @Autowired
    RuleRepository ruleRepo;

    @Autowired
    FrameRepository frameRepo;
    
    @Autowired
    UserRepository userRepository;
    
    /*** donne le résultat du lancer suivant pour ce joueur
    *   @param quilles donne le nombre de quilles abattues par ce lancer 
        ***/
        public Game lancer(Long gameId, int pins) {

        Game game = this.gameRepo.findById(gameId).orElseThrow(() -> new GameNotFoundException());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPlayer currentUser = this.userRepository.findByUsername(auth.getName()).orElseThrow();

        Rule rule = game.getRule();
        
        if(!game.getPlayers().contains(currentUser)) throw new NoPlayerInGameException();

        // get last frame
        Frame currentFrame = game.getPlayerCurrentFrame(currentUser);
        if(currentFrame == null) {
            currentFrame = this.createNextFrame(currentUser, game);
            game.getFrames().add(currentFrame);
        }

        // Random pins down if no pins have been added to the request
        if(pins < 0) {
            int pinsDown = currentFrame.getPinsDown();
            pins = rand.nextInt((rule.getMaxPins() - pinsDown) + 1);
        }

        if (currentFrame.getFrameNumber() == rule.getMaxFrames()) {
            GameLogics.handleFinalFrameRoll(rule, currentFrame, pins);
        } else {
            GameLogics.handleRegularFrameRoll(rule, currentFrame, pins);
        }

        GameLogics.updateGameScoreForPlayer(game, currentUser);

        return this.gameRepo.save(game);
    }

    private Frame createNextFrame(UserPlayer player, Game game) {
        int currentFrameNumber = Math.toIntExact(game.getFrames().stream().filter(v -> v.getPlayer().equals(player)).count())+1;

        if (currentFrameNumber > game.getRule().getMaxFrames()) {
            throw new NoMoreFrameAllowedException();
        }
    
        Frame frame = new Frame();
        frame.setFrameNumber(currentFrameNumber);
        frame.setGame(game);
        frame.setPlayer(player);
        frame.setRolls(new ArrayList<Integer>());
    
        return this.frameRepo.save(frame);
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

    public Game newGame(Long gameRuleId, String gameName) {
        Rule rule = this.ruleRepo.findById(gameRuleId).orElseThrow();
        Game game = new Game();
        game.setRule(rule);
        game.setName(gameName);
        return this.gameRepo.save(game);
    }

    public void deleteGame(Long gameId) {
        this.gameRepo.deleteById(gameId);
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
}
