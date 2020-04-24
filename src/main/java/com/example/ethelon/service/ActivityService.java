package com.example.ethelon.service;

import com.example.ethelon.dao.ActivityDao;
import com.example.ethelon.model.Activity;
import com.example.ethelon.model.ActivityCriteria;
import com.example.ethelon.model.ActivitySkill;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.ethelon.utility.Constants.ONE;

/**
 * Service for activity. Business logic for Activity interactions are placed here
 * @author Kobe Kyle Relativo
 */
@Service
public class ActivityService {
    /**
     * Dao for activity
     */
    private ActivityDao activityDao;

    /**
     * Constructor for Unit testing
     * @param mockedService Mocked service to be injected
     */
    ActivityService(final ActivityDao mockedService){
        activityDao = mockedService;
    }

    /**
     * Function that retrieves all activities for User Home screen
     * @return List of Activities
     */
    public List<Activity> getActivitiesForUserHomePage(final String volunteerId, final int offset){
        final List<Activity> activitiesNotDone = activityDao.getActivitiesNotDone(offset);
        final List<Activity> actVolSkillsCount = new ArrayList<>();
        for(final Activity activity : activitiesNotDone){

            final String activityId = activity.getActivity_id();
            final List<ActivityCriteria> criteria = activityDao.getActivityCriteria(activityId);
            activity.setActivity_criteria(criteria);
            final List<ActivitySkill> skills = activityDao.getActivitySkills(activityId);
            activity.setActivity_skills(skills);
            //FIXME HashMap
            final HashMap<String, String> countAndJoin = activityDao.getActVolCountAndVolJoined(
                    volunteerId, activityId);
            activity.setVolunteer_count(Integer.parseInt(countAndJoin.get("count")));
            //FIXME Bad code but because of old API
            activity.setVolunteerstatus(Integer.parseInt(countAndJoin.get("volJoin")) == ONE ? "yes" : "no");
            final int skillsCount = activityDao.skillsMatchedCount(activityId, volunteerId);
            if(skillsCount > 0){
                activity.setMatchedSkillsWithUser(skillsCount);
                actVolSkillsCount.add(activity);
            }
        }
        //this loop removes the activities with matched skills temporarily from list of activities
        for(final Activity activity: actVolSkillsCount){
            activitiesNotDone.remove(activity);
        }

        //Combine list of matched and unmatched
        activitiesNotDone.addAll(sortActivitiesAccordingToSkillCount(actVolSkillsCount));
        //Reverse list to make the sorted activities with matched skills as the front of the merged list
        Collections.reverse(activitiesNotDone);

        return activitiesNotDone;
    }

    /**
     * Sort activities accord to matched skills with the volunteer using descending order
     * @param activities HashMap of activity together with the skill count
     * @return sorted list of activities according to skills matched
     */
    @VisibleForTesting
     List<Activity> sortActivitiesAccordingToSkillCount(final List<Activity> activities){
        activities.sort((activity1, activity2) -> Integer.compare(activity2.getMatchedSkillsWithUser(),
                activity1.getMatchedSkillsWithUser()));
        return activities;
    }
}
