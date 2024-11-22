package com.telemis.exercice.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.telemis.exercice.exceptions.NoMoreRollAllowedException;
import com.telemis.exercice.models.Frame;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.models.UserPlayer;

public class GameLogics {

	public static void handleRegularFrameRoll(Rule rule, Frame currentFrame, int pins) {
        
        if(currentFrame.getRolls().size() > rule.getMaxRollsPerFrame()) {
            throw new NoMoreRollAllowedException();
        }

        currentFrame.getRolls().add(pins);

        // Check for strike or spare
        if (currentFrame.getRolls().get(0) == rule.getMaxPins() //STRIKE
            ||
            currentFrame.getPinsDown() == rule.getMaxPins() //Spare
        ) {
                for(int i = currentFrame.getRolls().size(); i < rule.getMaxRollsPerFrame() ; i++) {
                    currentFrame.getRolls().add(null);
                }
        }
    }

    public static void handleFinalFrameRoll(Rule rule, Frame currentFrame, int pins) {
        if(currentFrame.getRolls().size() > 0 && currentFrame.getRolls().get(0) == rule.getMaxPins()){
            if(currentFrame.getRolls().size() > (rule.getMaxRollsPerFrame() + rule.getStrikeAfterRolls())) {
                throw new NoMoreRollAllowedException();
            }
        }
        else if(currentFrame.getPinsDown() == rule.getMaxPins()) {
            if(currentFrame.getRolls().size() > (rule.getMaxRollsPerFrame() + rule.getSpareAfterRolls())) {
                throw new NoMoreRollAllowedException();
            }
        }
        currentFrame.getRolls().add(pins);
    }


	public static void updateGameScoreForPlayer(Game game, UserPlayer player) {
        Rule rule = game.getRule();
        
        // Get all frames for this player in order
        List<Frame> playerFrames = game.getFrames().stream()
            .filter(f -> f.getPlayer().equals(player))
            .sorted((f1, f2) -> Integer.compare(f2.getFrameNumber(),f1.getFrameNumber())) // reversed array !!!
            .toList();

        Queue<Integer> extraRolls = new LinkedList<>();

        for (int frameIndex = 0; frameIndex < playerFrames.size(); frameIndex++) {
            Frame frame = playerFrames.get(frameIndex);
            int frameScore = 0;
            boolean spareCheck = false;

            for(int rollIndex = frame.getRolls().size()-1; rollIndex >= 0 ; rollIndex--) { // reversed rolls !!!
                Integer roll = frame.getRolls().get(rollIndex);

                if(roll == null) continue;

                // Handle strike
                if (roll == rule.getMaxPins() && rollIndex == 0 && extraRolls.size() >= rule.getStrikeAfterRolls()) {
                    frameScore += extraRolls.stream().limit(rule.getStrikeAfterRolls()).mapToInt(r -> r.intValue()).sum();
                    frameScore += rule.getStrikeBonus();
                }
                // Handle spare
                else if (frame.getPinsDown() == rule.getMaxPins() && extraRolls.size() >= rule.getSpareAfterRolls() && !spareCheck) {
                    frameScore += extraRolls.stream().limit(rule.getSpareAfterRolls()).mapToInt(r -> r.intValue()).sum();
                    frameScore += rule.getSpareBonus();
                    spareCheck = true;
                }
                else if(!spareCheck) {
                    frameScore += roll;
                }
                
                extraRolls.add(roll);
                if(extraRolls.size() > rule.getMaxExtraRolls()) extraRolls.poll();
            }
            frame.setTotalScore(frameScore);
        }

        int previousScore = 0;
        for (int frameIndex = playerFrames.size()-1; frameIndex >= 0 ; frameIndex--) {
            Frame frame = playerFrames.get(frameIndex);
            previousScore += frame.getTotalScore();
            frame.setTotalScore(previousScore);
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
