package com.telemis.exercice.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Frame {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int frameNumber;

    private List<Integer> rolls;

    private int totalScore;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private UserPlayer player;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public int getPinsDown() {
        return this.getRolls().stream().filter(v -> v != null).mapToInt(r -> r.intValue()).sum();
    }
}
