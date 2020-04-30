package com.example.ethelon.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model for settings
 * @author Kobe Kyle Relativo
 */
@Data
@AllArgsConstructor
public class Setting {
    private int id;
    private int activityPresetPoints;
    private int activityHoursRenderedMultiplier;
    private int activityPointsPerRating;
    private int newbieGauge;
    private int explorerGauge;
    private int expertGauge;
    private int legendGauge;
    private int agePercentage;
    private int pointPercentage;
    private int ageTotal;
    private int pointTotal;
}
