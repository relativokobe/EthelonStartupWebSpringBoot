package com.example.ethelon.service;

import com.example.ethelon.dao.VolunteerDao;
import com.example.ethelon.model.Skill;
import com.example.ethelon.model.Volunteer;
import com.example.ethelon.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for user. Business logic for Volunteer interactions are placed here
 * @author Kobe Kyle Relativo
 */
@Service
public class VolunteerService {
    /**
     * Dao for Volunteer
     */
    @Autowired
    private VolunteerDao volunteerDao;

    /**
     * Service that calls DAO to insert Volunteer. Can only be called in User registration
     * @param volunteer Volunteer to be inserted
     */
    public void insertVolunteerToDb(final Volunteer volunteer){
        volunteerDao.insertVolunteerToDb(volunteer);
        //Newly inserted volunteer also needs to insert volunteer badge
        insertVolunteerBadge(volunteer.getVolunteerId());
    }

    /**
     * Function to insert data to volunteer badge. Loop is needed to insert each skill for the volunteer
     * @param volunteerId id of the volunteer
     */
    private void insertVolunteerBadge(final String volunteerId){
        final List<Skill> skills = Constants.getSkills();
        for (Skill skill : skills) {
            //Insert each skill to table.
            volunteerDao.insertVolunteerBadge(volunteerId, String.valueOf(skill));
        }
    }

    /**
     * Function to insert volunteer skills
     * @param volunteerId id of the volunteer
     * @param skills list of skills to be inserted
     */
    public void insertVolunteerSkills(final String volunteerId, final List<String> skills){
        volunteerDao.insertVolunteerSkills(volunteerId, skills);
    }

    /**
     * Function to insert volunteer to activity
     * @param volunteerId ID of the volunteer
     * @param activityId ID of the activity
     */
    public void joinActivity(final String volunteerId, final String activityId){
        volunteerDao.joinActivity(volunteerId, activityId, Constants.getCurrentTimeInString());
    }

    /**
     * Function to check if user already joined activity
     * @param volunteerId ID of the volunteer
     * @param activityId ID of the activity
     * @return boolean if user already joined activity or not
     */
    public boolean volunteerJoinedActivity(final String volunteerId, final String activityId){
        return volunteerDao.volunteerJoinedActivity(volunteerId, activityId);
    }
}
