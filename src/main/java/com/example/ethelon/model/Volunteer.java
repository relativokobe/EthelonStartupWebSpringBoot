package com.example.ethelon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Volunteer model
 * @author Kobe Kyle Relativo
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Volunteer extends User{
    private String volunteerId;
    private String location;
    private String imageUrl;
    private String fcmToken;
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
     * @param volunteerId ID of the volunteer
     * @param location location of the volunteer
     * @param imageUrl image_url of the volunteer
     * @param fcmToken fcm_token of the volunteer
     * @param age age of the volunteer
     * @param points points of the volunteer
     */
    public Volunteer(final String userId, final String name, final String email, final String password,
                     final String role, final String apiToken, final String volunteerId, final String location,
                     final String imageUrl, final String fcmToken, final int age, final int points) {
        //Initialize Parent (User)
        super(userId, name, email, password, role, apiToken);
        this.volunteerId = volunteerId;
        this.location = location;
        this.imageUrl = imageUrl;
        this.fcmToken = fcmToken;
        this.age = age;
        this.points = points;
    }

}
