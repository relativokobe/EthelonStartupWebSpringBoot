package com.example.ethelon.model;

import lombok.Data;

import java.util.List;

/**
 * Activity model
 * @author Kobe Kyle Relativo
 */
@Data
public class Activity extends UserInteraction {
    private String activity_id;
    private String foundation_id;
    private String name;
    private String image_url;
    private String imageQr_url;
    private String description;
    private String location;
    private String start_time;
    private String end_time;
    private int group;
    private String Long;
    private String lat;
    private int points_equivalent;
    private int status;
    private String created_at;
    private String update_at;
    private String contactperson;
    private String contact;
    private String startDate;
    private String foundtion_name;
    private String volunteerstatus;
    private String foundation_img;
    private int volunteer_count;
    private List<ActivitySkill> activity_skills;
    private List<ActivityCriteria> activity_criteria;
}
