package com.example.ethelon.dao;

import com.example.ethelon.model.LeaderBoardVolunteer;
import com.example.ethelon.model.Volunteer;
import com.example.ethelon.model.VolunteerBadgeInfo;
import com.example.ethelon.model.VolunteerToRate;
import com.example.ethelon.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.ethelon.utility.Constants.DEFAULT_VOLUNTEER_POINTS;
import static com.example.ethelon.utility.Constants.ZERO;

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
        final Object[] args = new Object[]{volunteer.getVolunteer_id(), volunteer.getUserId(), volunteer.getLocation(),
                volunteer.getImage_url(), volunteer.getFcm_token(), ZERO, volunteer.getAge(), volunteer.getPoints()};
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
        final Object[] args =  new Object[]{Constants.NOTHING, volunteerId, ZERO, ZERO, skill,
            ZERO};
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

    /**
     * Function to insert to DB the details of volunteer joining the activity
     * @param volunteerId ID of the volunteer
     * @param activityId ID of the activity
     */
    public void joinActivity(final String volunteerId, final String activityId, final String timeNow){
        final Object[] args = new Object[]{volunteerId, activityId, DEFAULT_VOLUNTEER_POINTS, ZERO, timeNow};
        final String query = "INSERT INTO volunteeractivities (volunteer_id, activity_id, points, status, timeIn) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, args);
    }

    /**
     * Function to check if user already joined activity
     * @param volunteerId ID of the volunteer
     * @param activityId ID of the activity
     * @return boolean if user already joined activity or not
     */
    public boolean volunteerJoinedActivity(final String volunteerId, final String activityId){
        final Object[] args = new Object[]{volunteerId, activityId};
        final Integer count =  jdbcTemplate.query("SELECT COUNT(*) AS joined_count FROM volunteeractivities " +
                "WHERE volunteer_id = ? AND activity_id = ?", args, resultSet -> resultSet.next() ?
                resultSet.getInt("joined_count") : 0);
        if(count == null){
            return false;
        }else{
            return count > 0;
        }
    }

    /**
     * Function to retrieve the activity group ID of the volunteer on an activity
     * @param volunteerId ID of the volunteers
     * @param activityId ID of the activity
     * @return Activity group ID of the volunteer in an activity
     */
    public List<VolunteerToRate> volunteersToRate(final String volunteerId, final String activityId){
        final Object[] args = new Object[]{volunteerId, activityId};
        final String query = "SELECT activitygroups.* FROM activitygroups INNER JOIN volunteergroups ON " +
                "volunteergroups.activity_groups_id = activitygroups.id WHERE volunteergroups.volunteer_id =" +
                " ? AND activitygroups.activity_id = ? ";
        return jdbcTemplate.query(query, args, resultSet -> {
            List<VolunteerToRate> volunteers = new ArrayList<>();
            if(resultSet.next()){
                volunteers = getVolunteersToRate(resultSet.getString("numOfVolunteers"),
                        resultSet.getString("type"), volunteerId, resultSet.getString("id"));
            }
            return volunteers;
        });
    }

    /**
     * Function to retrieve volunteers to Rate of current volunteer
     * @param volunteerId ID of the volunteer
     * @param activityGroupId ID of the volunteer's activityGroup
     * @return list of volunteers to be rated by current volunteer
     */
    private List<VolunteerToRate> getVolunteersToRate(final String numOfVolunteers,
                                                      final String type, final String volunteerId,
                                                      final String activityGroupId){
        final Object[] args = new Object[]{activityGroupId, volunteerId};
        final String query = "SELECT users.name, volunteers.volunteer_id, volunteers.image_url" +
                " FROM users INNER JOIN volunteers ON users.user_id = volunteers.user_id INNER JOIN" +
                " volunteergroups ON volunteers.volunteer_id = volunteergroups.volunteer_id" +
                " WHERE volunteergroups.activity_groups_id = ? AND volunteergroups.volunteer_id != ?";

        return jdbcTemplate.query(query, args, resultSet -> {
            final List<VolunteerToRate> volunteers = new ArrayList<>();
            while(resultSet.next()){
                final VolunteerToRate volunteer = new VolunteerToRate(resultSet.getString("name"), resultSet.
                        getString("volunteer_id"), resultSet.getString("image_url"), activityGroupId,
                        numOfVolunteers, type);
                volunteers.add(volunteer);
            }
            return volunteers;
        });
    }

    /**
     * Retrieve volunteer bades info of current volunteer
     * @param volunteerId ID of the volunteers
     * @return list of VolunteerBadgeInfo
     */
    public List<VolunteerBadgeInfo> getVolunteerBadgesInfo(final String volunteerId){
        final Object[] args = new Object[]{volunteerId};
        final String query = "Select volunteerbadges.*, badges.url as url, badges.* FROM volunteerbadges INNER JOIN " +
                "badges  ON volunteerbadges.badge = badges.badge AND volunteerbadges.skill = badges.skill WHERE " +
                "volunteerbadges.volunteer_id = ?";
        return jdbcTemplate.query(query, args, resultSet -> {
            final List<VolunteerBadgeInfo> badgesInfo = new ArrayList<>();
            while(resultSet.next()){
                final VolunteerBadgeInfo info = new VolunteerBadgeInfo();
                info.setBadge(resultSet.getString("badge"));
                info.setVolunteer_id(resultSet.getString("volunteer_id"));
                info.setStar(resultSet.getInt("star"));
                info.setPoints(resultSet.getInt("points"));
                info.setGaugeExp(resultSet.getInt("gaugeExp"));
                info.setUrl(resultSet.getString("url"));
                info.setId(resultSet.getInt("id"));
                info.setSkill(resultSet.getString("skill"));
                info.setBadge_name(resultSet.getString("badge_name"));
                badgesInfo.add(info);
            }
            return badgesInfo;
        });
    }

    /**
     * Retrieve list of volunteers sorted for leaderboard
     * @return List of LeaderBoardVolunteer
     */
    public List<LeaderBoardVolunteer> getVolunteersForLeaderboard(){
        final String query = "SELECT users.name AS name, volunteers.*, users.api_token AS api_token FROM volunteers " +
                "INNER JOIN users ON volunteers.user_id = users.user_id ORDER BY volunteers.points DESC";
        return jdbcTemplate.query(query, resultSet -> {
            final List<LeaderBoardVolunteer> volunteers = new ArrayList<>();
            while(resultSet.next()){
                final LeaderBoardVolunteer volunteer = new LeaderBoardVolunteer();
                volunteer.setApiToken(resultSet.getString("api_token"));
                volunteer.setName(resultSet.getString("name"));
                volunteer.setAge(resultSet.getInt("age"));
                volunteer.setVolunteer_id(resultSet.getString("volunteer_id"));
                volunteer.setLocation(resultSet.getString("location"));
                volunteer.setPoints(resultSet.getInt("points"));
                volunteer.setFcm_token(resultSet.getString("fcm_token"));
                volunteers.add(volunteer);
            }
            return volunteers;
        });
    }

    /**
     * This function updates the user's FCM token in the DB
     * @param volunteerId ID of the volunteer
     * @param fcmToken FCM token of the volunteer
     */
    public void fcmToken(final String volunteerId, final String fcmToken){
        final String query = "UPDATE volunteers SET fcm_token = ? WHERE volunteer_id = ?";
        final Object[] args = new Object[]{fcmToken, volunteerId};
        jdbcTemplate.update(query, args);
    }

    /**
     * This function retrieves the Volunteer info using the user id
     * @param userId ID of the user
     * @return retrieved volunteer object using user ID
     * TODO add more info in query to retrieve more info
     */
    public Volunteer retrieveVolunteerUsingUserId(final String userId){
        final Object[] args = new Object[]{userId};
        final String query = "SELECT volunteer_id, api_token FROM users INNER JOIN volunteers ON " +
                "users.user_id = volunteers.user_id WHERE users.user_id = ?";

        return jdbcTemplate.query(query, args, resultSet -> {
            Volunteer volunteer = null;
            if(resultSet.next()){
                volunteer = new Volunteer();
                volunteer.setApiToken(resultSet.getString("api_token"));
                volunteer.setVolunteer_id(resultSet.getString("volunteer_id"));
            }
            return volunteer;
        });
    }
}
