package com.telemis.exercice.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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

        // get last frame
        Frame currentFrame = game.getPlayerCurrentFrame(null);
        if (currentFrame == null) {
            throw new IllegalStateException("No current frame found or game is complete.");
        }

        if (currentFrame.getFrameNumber() == rule.getMaxFrames()) {
            handleFinalFrameRoll(rule, currentFrame, pins);
        } else {
            handleRegularFrameRoll(rule, currentFrame, pins);
        }

        // Update frame score
        updateFrameScore(currentFrame);

        // Return updated frame
        this.frameRepo.save(currentFrame);

        return game;
    }

    private void handleRegularFrameRoll(Rule rule, Frame currentFrame, int pins) {
        
        if(currentFrame.getRolls().size() >= rule.getMaxRollsPerFrame()) {
            throw new IllegalStateException("No more rolls allowed in this frame.");
        }

        // Check for strike or spare
        if (currentFrame.getRolls().get(1) == rule.getMaxPins() //STRIKE
            ||
            currentFrame.getRolls().stream().mapToInt(r -> r.intValue()).sum() == rule.getMaxPins() //Spare
        ) {
                for(int i = currentFrame.getRolls().size(); i < rule.getMaxRollsPerFrame() ; i++) {
                    currentFrame.getRolls().add(-0);
                }
        }
    }

    private void handleFinalFrameRoll(Rule rule, Frame currentFrame, int pins) {
        
        if(currentFrame.getRolls().get(1) == rule.getMaxPins()) { //Strike so allow up to rule.getMaxRollsPerFrame() + 1
            
            if(currentFrame.getRolls().size() >= rule.getMaxRollsPerFrame() + 1) {
                throw new IllegalStateException("No more rolls allowed in the final frame.");
            }
            
            currentFrame.getRolls().add(pins);

        }
            //Spare so allow up to rule.getMaxRollsPerFrame() + 2
        
        int finalScore = currentFrame.getRolls().stream().mapToInt(i -> i.intValue()).sum();
        boolean allPinsDown = rule.getMaxPins() == finalScore;


        if(allPinsDown && currentFrame.getRolls().size() >= rule.getMaxRollsPerFrame() + 2) {
            currentFrame.getRolls().add(pins);
        }
    }

    private void updateFrameScore(Frame currentFrame) {
        int score = currentFrame.getRolls().stream().mapToInt(i -> i.intValue()).sum();
        currentFrame.setTotalScore(score);

        // Update total score for the game (accumulate from previous frames)
        Game game = currentFrame.getGame();

         int totalScore = game.getFrames().stream()
            .filter(g -> g.getPlayer().equals(currentFrame.getPlayer()))
            .flatMapToInt(g -> g.getRolls().stream().mapToInt(r -> r.intValue())).sum();
    }

    private void updateScores(Game game, UserPlayer player) {
        List<Frame> frames = game.getFrames().stream()
            .filter(f -> f.getPlayer().equals(player))
            .sorted(Comparator.comparingInt(Frame::getFrameNumber))
            .collect(Collectors.toList());
    
        int cumulativeScore = 0;
    
        for (int i = 0; i < frames.size(); i++) {
            Frame currentFrame = frames.get(i);
    
            // Skip if frame is incomplete
            if (!currentFrame.isComplete(game.getRule())) {
                continue;
            }
    
            // Calculate the score for the current frame
            int frameScore = currentFrame.getRolls().stream().mapToInt(r -> r.intValue()).sum();
    
            // Add bonuses for strikes and spares
            if (currentFrame.getRolls().get(1) == 10) { // Strike
                frameScore += getNextRolls(game.getRule(), frames, i);
            } else if (frameScore == game.getRule().getMaxPins() && currentFrame.getRolls().size() <= game.getRule().getMaxRollsPerFrame()) { // Spare
                frameScore += getNextRolls(game.getRule(), frames, i);
            }
    
            // Update frame score and cumulative score
            cumulativeScore += frameScore;
            currentFrame.setTotalScore(cumulativeScore);
        }
    }

    private int getNextRolls(Rule rule, List<Frame> frames, int index) {
        int bonus = 0;
    
        // If the next frame exists, count its rolls
        if (index + 1 < frames.size()) {
            Frame nextFrame = frames.get(index + 1);
            bonus += nextFrame.getRolls().get(1);
    
            if (nextFrame.getRolls().get(1) == rule.getMaxPins() && index + 2 < frames.size()) {
                // If next frame is a strike, take roll1 of the frame after that
                bonus += frames.get(index + 2).getRolls().get(1);
            } else {
                // Otherwise, take roll2 from the same frame
                bonus += nextFrame.getRolls().get(2);
            }
        }
    
        return bonus;
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
