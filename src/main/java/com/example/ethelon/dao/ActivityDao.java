package com.example.ethelon.dao;

import com.example.ethelon.model.Activity;
import com.example.ethelon.model.ActivityCriteria;
import com.example.ethelon.model.ActivitySkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Dao for activity
 * @author Kobe Kyle Relativo
 */
@Repository
public class ActivityDao {
    /**
     * Used to connect to Database and do Queries
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Function to retrieve all activities that are done
     * @return list of activites not done
     */
    public List<Activity> getActivitiesNotDone(final int offset){
        final Object[] args = new Object[]{offset};
        final String query = "SELECT activities.*, users.name AS foundation_name, foundations.image_url AS " +
                "foundation_imageurl FROM foundations INNER JOIN activities ON foundations.foundation_id = " +
                "activities.foundation_id INNER JOIN users ON foundations.user_id = users.user_id WHERE " +
                "activities.status = false ORDER BY activities.startDate DESC LIMIT 5 OFFSET ?;";

        return jdbcTemplate.query(query, args, rs ->{
            final List<Activity> activities = new ArrayList<>();
            while(rs.next()){
                final Activity activity = new Activity();
                activity.setActivity_id(rs.getString("activity_id"));
                activity.setFoundation_id(rs.getString("foundation_id"));
                activity.setName(rs.getString("name"));
                activity.setImage_url(rs.getString("image_url"));
                activity.setImageQr_url(rs.getString("imageQr_url"));
                activity.setDescription(rs.getString("description"));
                activity.setLocation(rs.getString("location"));
                activity.setStart_time(rs.getString("start_time"));
                activity.setEnd_time(rs.getString("end_time"));
                activity.setStartDate(rs.getString("startDate"));
                activity.setGroup(rs.getInt("group"));
                activity.setLong(rs.getString("long"));
                activity.setLat(rs.getString("lat"));
                activity.setPoints_equivalent(rs.getInt("points_equivalent"));
                activity.setStatus(rs.getInt("status"));
                activity.setCreated_at(rs.getString("created_at"));
                activity.setContact(rs.getString("contact"));
                activity.setContactperson(rs.getString("contactperson"));
                activities.add(activity);
            }
            return activities;
        });
    }

    /**
     * Function to get activity skills of activity
     * @param activityId ID of the activity
     * @return get all the skills of an activity
     */
    public List<ActivitySkill> getActivitySkills(final String activityId){
        final Object[] args = new Object[]{activityId};
        final String query = "SELECT name, activity_id, created_at, updated_at FROM activityskills WHERE activity_id = ? ";

        return jdbcTemplate.query(query, args, rs -> {
            final List<ActivitySkill> activitySkills = new ArrayList<>();
            while(rs.next()){
                final ActivitySkill activitySkill = new ActivitySkill(rs.getString("name"), rs.
                        getString("activity_id"), rs.getString("created_at"), rs.getString("updated_at"));
                activitySkills.add(activitySkill);
            }

            return activitySkills;
        });
    }

    /**
     * Function to get activity criteria of activity
     * @param activityId ID of the activity
     * @return get all the skills of an activity
     */
    public List<ActivityCriteria> getActivityCriteria(final String activityId){
        final Object[] args = new Object[]{activityId};
        final String query = "SELECT criteria, activity_id FROM activitycriterias WHERE activity_id = ? ";

        return jdbcTemplate.query(query, args, rs -> {
            final List<ActivityCriteria> activityCriterias = new ArrayList<>();
            while(rs.next()){
                final ActivityCriteria activitySkill = new ActivityCriteria(rs.getString("activity_id"),
                        rs.getString("criteria"));
                activityCriterias.add(activitySkill);
            }

            return activityCriterias;
        });
    }

    /**
     * Function to count volunteers and see if Volunteer already joined the activity
     * @return count of volunteer
     */
    public HashMap<String, String> getActVolCountAndVolJoined(final String volunteerId, final String activityId){
        final Object[] args = new Object[]{volunteerId, activityId};
        final String query = "SELECT (SELECT COUNT(*) FROM volunteeractivities) AS count, (SELECT COUNT(*) FROM " +
                "volunteeractivities WHERE volunteer_id = ? AND activity_id = ?) AS volJoin";
        return jdbcTemplate.query(query, args, rs -> {
            final HashMap<String, String> hashMap = new HashMap<>();
            if(rs.next()){
                hashMap.put("count", String.valueOf(rs.getInt("count")));
                hashMap.put("volJoin", String.valueOf(rs.getInt("volJoin")));
            }
            return hashMap;
        });

    }

    /**
     * Function that count the matched skills of the volunteer and activity
     * @param activityId ID of the activity
     * @return list of activities criteria
     */
    public int skillsMatchedCount(final String activityId, final String volunteerId){
        final Object[] args = new Object[]{volunteerId, activityId};
        final String query = "SELECT count(*) AS count FROM activityskills INNER JOIN volunteerskills on " +
                "activityskills.name LIKE volunteerskills.name where activity_id = ? AND volunteer_id = ?";
        final Integer count =  jdbcTemplate.query(query, args, resultSet ->
            resultSet.next() ? resultSet.getInt("count") : 0);

        return count == null ? 0 : count;
    }

}
