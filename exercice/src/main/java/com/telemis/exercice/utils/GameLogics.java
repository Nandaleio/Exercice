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

        // Update total score for the game (accumulate from previous frames)
        Game game = currentFrame.getGame();

         int totalScore = game.getFrames().stream()
            .filter(g -> g.getPlayer().equals(currentFrame.getPlayer()))
            .flatMapToInt(g -> g.getRolls().stream().mapToInt(r -> r.intValue())).sum();
            
        currentFrame.setTotalScore(totalScore);
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
