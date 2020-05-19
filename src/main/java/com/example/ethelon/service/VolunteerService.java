package com.example.ethelon.service;

import com.example.ethelon.dao.SettingDao;
import com.example.ethelon.dao.SkillDao;
import com.example.ethelon.dao.VolunteerDao;
import com.example.ethelon.model.Skill;
import com.example.ethelon.model.Volunteer;
import com.example.ethelon.model.VolunteerToRate;
import com.example.ethelon.model.VolunteerBadgesInfoResponse;
import com.example.ethelon.model.VolunteerBadgeInfo;
import com.example.ethelon.model.Setting;
import com.example.ethelon.model.BadgeEnum;
import com.example.ethelon.model.Badge;
import com.example.ethelon.model.LeaderBoardVolunteer;
import com.example.ethelon.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * Dao for Skill
     */
    @Autowired
    private SkillDao skillDao;

    /**
     * Dao for Settings
     */
    @Autowired
    private SettingDao settingDao;

    /**
     * Service that calls DAO to insert Volunteer. Can only be called in User registration
     * @param volunteer Volunteer to be inserted
     */
    public void insertVolunteerToDb(final Volunteer volunteer){
        volunteerDao.insertVolunteerToDb(volunteer);
        //Newly inserted volunteer also needs to insert volunteer badge
        insertVolunteerBadge(volunteer.getVolunteer_id());
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

    /**
     * Function to retrieve volunteers to be rated by current volunteer in an activity
     * @param volunteerId ID of volunteer
     * @param activityId ID of activity
     * @return
     */
    public List<VolunteerToRate> retrieveVolunteersToRate(final String volunteerId, final String activityId){
        return volunteerDao.volunteersToRate(volunteerId, activityId);
    }

    /**
     * This function retrieves volunteer's profile
     * @param volunteerId ID of the volunteer
     * @return list of VolunteerBadgesInfoResponse
     */
    public List<VolunteerBadgesInfoResponse> retrieveVolunteerProfile(final String volunteerId){
        final List<VolunteerBadgeInfo> badgesInfo = volunteerDao.getVolunteerBadgesInfo(volunteerId);
        final List<VolunteerBadgesInfoResponse> volunteerBadgesInfo = new ArrayList<>();
        for(final VolunteerBadgeInfo info : badgesInfo){
            final List<Badge> badges = skillDao.getBadgesAccordingToSkill(info.getSkill());
            final int percentCompleted = getPercentCompleted(info.getStar(), info.getBadge(), info.getPoints());
            final VolunteerBadgesInfoResponse volunteerBadgeInfoResponse =
                    new VolunteerBadgesInfoResponse(badgesInfo, percentCompleted, badges);
            volunteerBadgesInfo.add(volunteerBadgeInfoResponse);
        }

        return volunteerBadgesInfo;
    }

    /**
     * This function computes the total percent completed by volunteer
     * @param stars number of stars
     * @param badge badge to compute
     * @param badgePoints current badgePoints
     * @return percentage completed of the badge
     */
    private int getPercentCompleted(final int stars, final String badge, final int badgePoints){
        final int gauge = getGauge(badge);
        //compute points. Multiply stars and gauge then add badge points
        final int points = (stars * gauge) + badgePoints;
        //compute percentage completed
        return (points / (gauge * 6)) * 100;
    }

    /**
     * This function retrieves the gaugePoints depending on Badge
     * @param badge to get the gauge from
     * @return gauge points
     */
    private int getGauge(final String badge){
        final Setting setting = settingDao.getSetting();
        final int gauge;
        switch(BadgeEnum.valueOf(badge)){
            case Nothing:
            case Newbie:
                gauge = setting.getNewbieGauge();
                break;
            case Explorer:
                gauge = setting.getExplorerGauge();
                break;
            case Expert:
                gauge = setting.getExpertGauge();
                break;
            case Legend:
                gauge = setting.getLegendGauge();
                break;
            default:
                gauge = 0;
        }
        return gauge;
    }

    /**
     * This function retrieves the volunteers sorted according to points for leaderboard
     * @return list of volunteers
     */
    public List<LeaderBoardVolunteer> getVolunteersForLeaderBoard(){
        return volunteerDao.getVolunteersForLeaderboard();
    }
}
