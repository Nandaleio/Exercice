package com.telemis.exercice.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.telemis.exercice.models.Frame;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.models.UserPlayer;

public class GameLogics {

	public static void handleRegularFrameRoll(Rule rule, Frame currentFrame, int pins) {
        
        if(currentFrame.getRolls().size() >= rule.getMaxRollsPerFrame()) {
            throw new IllegalStateException("No more rolls allowed in this frame.");
        }

        // Check for strike or spare
        if (currentFrame.getRolls().get(1) == rule.getMaxPins() //STRIKE
            ||
            currentFrame.getPinsDown() == rule.getMaxPins() //Spare
        ) {
                for(int i = currentFrame.getRolls().size(); i < rule.getMaxRollsPerFrame() ; i++) {
                    currentFrame.getRolls().add(-0);
                }
        }
    }

    public static void handleFinalFrameRoll(Rule rule, Frame currentFrame, int pins) {
        
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


	public static void updateFrameScore(Frame currentFrame) {
        int score = currentFrame.getRolls().stream().mapToInt(i -> i.intValue()).sum();
        currentFrame.setTotalScore(score);

        // Update total score for the game (accumulate from previous frames)
        Game game = currentFrame.getGame();

         int totalScore = game.getFrames().stream()
            .filter(g -> g.getPlayer().equals(currentFrame.getPlayer()))
            .flatMapToInt(g -> g.getRolls().stream().mapToInt(r -> r.intValue())).sum();
		System.out.println(totalScore);
    }
	
	public void updateScores(Game game, UserPlayer player) {
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
            int frameScore = currentFrame.getPinsDown();
    
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

	public int getNextRolls(Rule rule, List<Frame> frames, int index) {
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

}
