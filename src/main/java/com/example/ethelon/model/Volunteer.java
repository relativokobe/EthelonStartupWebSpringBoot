package com.example.ethelon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Volunteer model
 * @author Kobe Kyle Relativo
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Volunteer extends User{
    private String volunteer_id;
    private String location;
    private String image_url;
    private String fcm_token;
    private int points = 0;
    private int age = 1;

    /**
     * This constructor is used to initialize the Volunteer and its User details
     *
     * @param userId ID of the user
     * @param name name of the user
     * @param email email of the user
     * @param password password of te user
     * @param role role of the user
     * @param apiToken api_token of the user
     * @param volunteer_id ID of the volunteer
     * @param location location of the volunteer
     * @param image_url image_url of the volunteer
     * @param fcm_token fcm_token of the volunteer
     * @param age age of the volunteer
     * @param points points of the volunteer
     */
    public Volunteer(final String userId, final String name, final String email, final String password,
                     final String role, final String apiToken, final String volunteer_id, final String location,
                     final String image_url, final String fcm_token, final int age, final int points) {
        //Initialize Parent (User)
        super(userId, name, email, password, role, apiToken);
        this.volunteer_id = volunteer_id;
        this.location = location;
        this.image_url = image_url;
        this.fcm_token = fcm_token;
        this.age = age;
        this.points = points;
    }

}
