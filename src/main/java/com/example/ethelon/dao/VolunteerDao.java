package com.example.ethelon.dao;

import com.example.ethelon.model.Volunteer;
import com.example.ethelon.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Function to insert volunteer skills to DB
     * @param volunteerId id of the volunteer
     * @param skills list of skills to be inserted to db
     */
    public void insertVolunteerSkills(final String volunteerId, final List<String> skills){
        String query = "INSERT INTO volunteerskills (name, volunteer_id) VALUES ";
        final int skillSize = skills.size();
        final int paramsSize = skillSize * 2;
        final StringBuilder params = new StringBuilder();
        int indexParamCounter = 0;
        final Object[] args = new Object[paramsSize];
        //input params and write query
        for (int i = 0; i < skillSize; i++) {
            args[indexParamCounter] = skills.get(i);
            indexParamCounter++;
            args[indexParamCounter] = volunteerId;
            indexParamCounter++;
            //write query for params
            params.append("(?, ?)").append(i + 1 == skillSize ? ";" : ",");
        }
        query = query + params.toString();
        jdbcTemplate.update(query, args);
    }
}
