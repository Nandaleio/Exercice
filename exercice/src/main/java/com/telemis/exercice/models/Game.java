package com.telemis.exercice.models;

import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "rule_id", referencedColumnName = "id")
    private Rule rule;

    @ManyToMany
    @JoinTable(
        name = "game_players",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<UserPlayer> players;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Frame> frames;


    public Frame getPlayerCurrentFrame(UserPlayer player) {
        if(this.getFrames() == null) return null;

        List<Frame> frames = this.getFrames().stream()
                .filter(f -> f.getPlayer().equals(player))
                .sorted(Comparator.comparingInt(Frame::getFrameNumber))
                .collect(Collectors.toList());

        for (Frame frame : frames) {
            if(frame.getRolls().size() < this.getRule().getMaxRollsPerFrame()) {
                return frame;
            }
        }
        return null;
    }
}
