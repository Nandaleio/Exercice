package com.telemis.exercice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int maxPins;

    private int maxFrames;

    private int maxRollsPerFrame;

    private int strikeBonus;
    private int strikeAfterPins;

    private int spareBonus;
    private int spareAfterPins;


    public int getMaxExtraRolls() {
        return Math.max(this.getSpareAfterPins(), this.getStrikeAfterPins()) -1;
    }
}
