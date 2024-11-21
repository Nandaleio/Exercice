package com.telemis.exercice.utils;

import java.util.List;

import com.telemis.exercice.models.Frame;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;

public class GameLogics {

	public static void handleRegularFrameRoll(Rule rule, Frame currentFrame, int pins) {
        
        if(currentFrame.getRolls().size() > rule.getMaxRollsPerFrame()) {
            throw new IllegalStateException("No more rolls allowed in this frame.");
        }

        currentFrame.getRolls().add(pins);

        // Check for strike or spare
        if (currentFrame.getRolls().get(0) == rule.getMaxPins() //STRIKE
            ||
            currentFrame.getPinsDown() == rule.getMaxPins() //Spare
        ) {
                for(int i = currentFrame.getRolls().size(); i < rule.getMaxRollsPerFrame() ; i++) {
                    currentFrame.getRolls().add(-0);
                }
        }
    }

    public static void handleFinalFrameRoll(Rule rule, Frame currentFrame, int pins) {
        if(currentFrame.getRolls().size() > rule.getMaxRollsPerFrame() + rule.getMaxExtraRolls()) {
            throw new IllegalStateException("No more rolls allowed in the final frame.");
        }
        
        currentFrame.getRolls().add(pins);
        
        if(currentFrame.getRolls().get(0) == rule.getMaxPins()) { //Strike so allow up to rule.getMaxRollsPerFrame() + 1
            
            if(currentFrame.getRolls().size() >= rule.getMaxRollsPerFrame() + 1) {
                throw new IllegalStateException("No more rolls allowed in the final frame.");
            }

        }
        
        int finalScore = currentFrame.getRolls().stream().mapToInt(i -> i.intValue()).sum();
        boolean allPinsDown = rule.getMaxPins() == finalScore;


        if(allPinsDown && currentFrame.getRolls().size() >= rule.getMaxRollsPerFrame() + 2) {
            currentFrame.getRolls().add(pins);
        }
    }


	public static void updateFrameScore(Frame currentFrame) {
        Game game = currentFrame.getGame();
        Rule rule = game.getRule();
        
        // Get all frames for this player in order
        List<Frame> playerFrames = game.getFrames().stream()
            .filter(f -> f.getPlayer().equals(currentFrame.getPlayer()))
            .sorted((f1, f2) -> Integer.compare(f1.getFrameNumber(), f2.getFrameNumber()))
            .toList();

        int runningTotal = 0;
        
        for (int i = 0; i < playerFrames.size(); i++) {
            Frame frame = playerFrames.get(i);
            int frameScore = frame.getPinsDown();
            
            // Handle strike bonus
            if (frame.getRolls().size() > 0 && frame.getRolls().get(0) == rule.getMaxPins()) {
                if (i < playerFrames.size() - 1) {
                    Frame nextFrame = playerFrames.get(i + 1);
                    frameScore += nextFrame.getRolls().stream()
                        .limit(rule.getStrikeAfterRolls())
                        .mapToInt(Integer::intValue)
                        .sum();
                }
            }
            // Handle spare bonus
            else if (frame.getPinsDown() == rule.getMaxPins()) {
                if (i < playerFrames.size() - 1) {
                    Frame nextFrame = playerFrames.get(i + 1);
                    if (!nextFrame.getRolls().isEmpty()) {
                        frameScore += nextFrame.getRolls().get(0) * rule.getSpareAfterRolls();
                    }
                }
            }
            
            runningTotal += frameScore;
            
            if (frame.equals(currentFrame)) {
                break;
            }
        }
        
        currentFrame.setTotalScore(runningTotal);
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
