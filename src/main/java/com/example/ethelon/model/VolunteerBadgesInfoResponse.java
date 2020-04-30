package com.example.ethelon.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Model to accomodate badges, volunteerbadge info and percent completed
 * @author Kobe Kyle Relativo
 */
@Data
@AllArgsConstructor
public class VolunteerBadgesInfoResponse {
    private List<VolunteerBadgeInfo> info;
    private int percentCompleted;
    private List<Badge> badges;
}
