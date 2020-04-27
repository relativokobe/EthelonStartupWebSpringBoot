package com.example.ethelon.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model for Volunteers to Rate
 * @author Kobe Kyle Relativo
 */
@Data
@AllArgsConstructor
public class VolunteerToRate {
    private final String name;
    private final String volunteer_id;
    private final String image_url;
    private final String activity_group_id;
    private final String num_of_vol;
    private final String type;
}
