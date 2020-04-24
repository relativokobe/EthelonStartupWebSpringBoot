package com.example.ethelon.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Activity skill model
 * @author Kobe Kyle Relativo
 */
@Data
@AllArgsConstructor
public class ActivitySkill {
    private String name;
    private String activity_id;
    private String created_at;
    private String updated_at;
}
