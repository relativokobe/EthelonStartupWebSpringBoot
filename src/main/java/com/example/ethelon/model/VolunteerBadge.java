package com.example.ethelon.model;

import lombok.Data;

/**
 * Model for VolunteerBadge
 * @author Kobe Kyle Relativo
 */
@Data
public class VolunteerBadge extends Badge{
    private String volunteer_id;
    private int star;
    private int points;
    private int gaugeExp;
}
