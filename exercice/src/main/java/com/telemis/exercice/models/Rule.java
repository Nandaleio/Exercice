package com.telemis.exercice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int maxPins;

    private int maxFrames;

    private int maxRollsPerFrame;

    private int strikeBonus;
    private int strikeAfterRolls;

    private int spareBonus;
    private int spareAfterRolls;


    public int getMaxExtraRolls() {
        return Math.max(this.getSpareAfterRolls(), this.getStrikeAfterRolls());
    }
}
