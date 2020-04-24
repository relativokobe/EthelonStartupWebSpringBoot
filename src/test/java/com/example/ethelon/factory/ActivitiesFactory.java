package com.example.ethelon.factory;

import com.example.ethelon.model.Activity;
import com.example.ethelon.model.ActivityCriteria;
import com.example.ethelon.model.ActivitySkill;
import com.example.ethelon.model.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for sample activities for Testing
 * @author Kobe Kyle Relativo
 */
public class ActivitiesFactory {

    /**
     * Function that produces sample activities for Testing
     * @return list of activities
     */
    public static List<Activity> getActivitiesNotDone(){
        final List<Activity> activities = new ArrayList<>();
        final Activity activity1 = new Activity();
        activity1.setActivity_id("1");
        activity1.setName("Sample name 1");
        activity1.setStartDate("2018-03-04 00:03:01");
        activities.add(activity1);

        final Activity activity2 = new Activity();
        activity2.setActivity_id("2");
        activity2.setName("Sample name 1");
        activity2.setStartDate("2018-03-04 00:03:01");
        activities.add(activity2);

        final Activity activity3 = new Activity();
        activity3.setActivity_id("3");
        activity3.setName("Sample name 1");
        activity3.setStartDate("2018-03-04 00:03:01");
        activities.add(activity3);

        final Activity activity4 = new Activity();
        activity4.setActivity_id("4");
        activity4.setName("Sample name 1");
        activity4.setStartDate("2018-03-04 00:03:01");
        activities.add(activity4);

        final Activity activity5 = new Activity();
        activity5.setActivity_id("5");
        activity5.setName("Sample name 1");
        activity5.setStartDate("2018-03-04 00:03:01");
        activities.add(activity5);

        return activities;
    }

    /**
     * Function that returns sample activity criteria for testing
     * @param activityId activity ID
     * @return List of activity criteria
     */
    public static List<ActivityCriteria> getActivityCriteria(final String activityId){
        final List<ActivityCriteria> activityCriteriaList = new ArrayList<>();
        final ActivityCriteria activityCriteria1 = new ActivityCriteria(activityId, "Criteria1");
        final ActivityCriteria activityCriteria2 = new ActivityCriteria(activityId, "Criteria2");
        activityCriteriaList.add(activityCriteria1);
        activityCriteriaList.add(activityCriteria2);
        return activityCriteriaList;
    }

    /**
     * This function returns activities with different matchedSkillsWithUser
     * @return List of activities
     */
    public static List<Activity> getActivitiesForSortSkillCount(){
        final List<Activity> activities = new ArrayList<>();
        final Activity activity1 = new Activity();
        activity1.setActivity_id("1");
        activity1.setName("sampleName");
        activity1.setMatchedSkillsWithUser(1);

        final Activity activity2 = new Activity();
        activity2.setActivity_id("2");
        activity2.setName("sampleName");
        activity2.setMatchedSkillsWithUser(2);

        final Activity activity3 = new Activity();
        activity3.setActivity_id("3");
        activity3.setName("sampleName");
        activity3.setMatchedSkillsWithUser(3);

        final Activity activity4 = new Activity();
        activity4.setActivity_id("4");
        activity4.setName("sampleName");
        activity4.setMatchedSkillsWithUser(4);

        final Activity activity5 = new Activity();
        activity5.setActivity_id("5");
        activity5.setName("sampleName");
        activity5.setMatchedSkillsWithUser(5);

        activities.add(activity2);
        activities.add(activity3);
        activities.add(activity1);
        activities.add(activity5);
        activities.add(activity4);

        return activities;
    }

    /**
     * Function that returns activities skill
     * @param activityId activity ID
     * @return List of activity skill
     */
    public static List<ActivitySkill> getActivitiesSkill(final String activityId){
        final List<ActivitySkill> activitySkillsList = new ArrayList<>();
        final ActivitySkill activitySkill1 = new ActivitySkill(String.valueOf(Skill.Sports),
                activityId, "sampleCreatedAT", "sampleUpdatedAt");
        final ActivitySkill activitySkill2 = new ActivitySkill(String.valueOf(Skill.Medical),
                activityId, "sampleCreatedAT", "sampleUpdatedAt");
        activitySkillsList.add(activitySkill1);
        activitySkillsList.add(activitySkill2);
        return activitySkillsList;
    }
}
