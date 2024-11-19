package com.telemis.exercice.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.telemis.exercice.models.Frame;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.UserPlayer;
import com.telemis.exercice.repositories.FrameRepository;

@Service
public class FrameService {

    private FrameRepository frameRepository;

    public Frame createNextFrame(UserPlayer player, Game game, int currentFrameNumber) {
        
        if (currentFrameNumber >= game.getRule().getMaxFrames()) {
            throw new IllegalArgumentException("No more frames allowed in the game.");
        }
    
        Frame frame = new Frame();
        frame.setFrameNumber(currentFrameNumber + 1);
        frame.setGame(game);
        frame.setPlayer(player);
        frame.setRolls(new ArrayList<Integer>());
        frame.setTotalScore(-1);
    
        return this.frameRepository.save(frame);
    }
}

