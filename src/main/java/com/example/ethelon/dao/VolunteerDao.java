package com.example.ethelon.dao;

import com.example.ethelon.model.Volunteer;
import com.example.ethelon.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Dao of Volunteer.
 * @author Kobe Kyle Relativo
 */
@Repository
public class VolunteerDao {
    /**
     * Used to connect to Database and do Queries
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * Function to insert volunteer to DB
     * @param volunteer volunteer to be inserted to DB
     */
    public void insertVolunteerToDb(final Volunteer volunteer){
        final String query = "INSERT INTO volunteers(volunteer_id, user_id, location, image_url, fcm_token, gaugeExp, age, points) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);" ;
        final Object[] args = new Object[]{volunteer.getVolunteerId(), volunteer.getUserId(), volunteer.getLocation(),
                volunteer.getImageUrl(), volunteer.getFcmToken(), Constants.ZERO, volunteer.getAge(), volunteer.getPoints()};
        jdbcTemplate.update(query, args);
    }

    /**
     * Function to insert volunteerbadge to DB
     * @param volunteerId ID of the volunteer
     * @param skill skill to be inserted
     */
    public void insertVolunteerBadge(final String volunteerId, final String skill){
        final String query = "INSERT INTO volunteerbadges(badge, volunteer_id, gaugeExp, star, skill, points) " +
                "VALUES(?, ?, ?, ?, ?, ?) ";
        final Object[] args =  new Object[]{Constants.NOTHING, volunteerId, Constants.ZERO, Constants.ZERO, skill,
            Constants.ZERO};
        jdbcTemplate.update(query, args);
    }
}
