package com.telemis.exercice.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telemis.exercice.exceptions.NoMoreRollAllowedException;
import com.telemis.exercice.models.Frame;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.models.UserPlayer;

public class GameLogics {

    Logger logger = LoggerFactory.getLogger(GameLogics.class);

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
            if(currentFrame.getRolls().size() > rule.getStrikeAfterRolls()) {
                throw new NoMoreRollAllowedException();
            }
        }
        else if(currentFrame.getPinsDown() >= rule.getMaxPins()) {

            int sum = 0;
            int spareRollIndex = 0;
            for(int i = 0 ; i < currentFrame.getRolls().size(); i++){
                sum += currentFrame.getRolls().get(i);
                if(sum >= rule.getMaxPins()) {
                    spareRollIndex = i;
                    break;
                }
            }

            if(currentFrame.getRolls().size() > (spareRollIndex + rule.getSpareAfterRolls())) {
                throw new NoMoreRollAllowedException();
            }
        } else if(currentFrame.getRolls().size() > rule.getSpareAfterRolls()) {
            throw new NoMoreRollAllowedException();
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

            int maxRolls = frame.getRolls().stream().filter(r -> r != null).collect(Collectors.toList()).size() -1;
            for(int rollIndex = maxRolls; rollIndex >= 0 ; rollIndex--) { // reversed rolls !!!
                Integer roll = frame.getRolls().get(rollIndex);

                if(roll == null) continue;

                // Handle strike
                if (roll == rule.getMaxPins() && rollIndex == 0 && extraRolls.size() >= rule.getStrikeAfterRolls()) {
                    // = only take the strike score (especialy for the last frame)
                    frameScore = extraRolls.stream().skip(Math.max(0, extraRolls.size()-rule.getStrikeAfterRolls())).mapToInt(r -> r.intValue()).sum();
                    frameScore += rule.getStrikeBonus();
                }
                // Handle spare
                else if (frame.getPinsDown() == rule.getMaxPins() && rollIndex == maxRolls && extraRolls.size() >= rule.getSpareAfterRolls() && !spareCheck) {
                    //need to take the x last elements !!
                    frameScore += extraRolls.stream().skip(Math.max(0, extraRolls.size()-rule.getSpareAfterRolls())).mapToInt(r -> r.intValue()).sum();
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
}
