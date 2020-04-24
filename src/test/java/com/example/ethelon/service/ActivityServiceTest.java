package com.example.ethelon.service;

import com.example.ethelon.dao.ActivityDao;
import com.example.ethelon.factory.ActivitiesFactory;
import com.example.ethelon.model.Activity;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for ActivityService class
 * @author Kobe Kyle Relativo
 */
final class ActivityServiceTest {
    /**
     * Class to be tested
     */
    private ActivityService activityService;

    /**
     * This function is used to test getActivitiesForUseHomePage function
     */
    @Test
    void testGetActivitiesForUserHomePage(){
        final ActivityDao activityDao = mock(ActivityDao.class);
        final int volunteerCount = 2;
        final int volunteerStatus = 1;
        final List<Activity> activitiesFromFactory = ActivitiesFactory.getActivitiesNotDone();
        //return activities using the mocked DAO
        when(activityDao.getActivitiesNotDone(anyInt())).thenReturn(activitiesFromFactory);
        //return activity criteria list using the mocked DAO
        when(activityDao.getActivityCriteria(anyString())).thenReturn(ActivitiesFactory.
                getActivityCriteria("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivityCriteria("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivityCriteria("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivityCriteria("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivityCriteria("activityIdForTesting"));
        //return activity skills list using the mocked DAO
        when(activityDao.getActivitySkills(anyString())).thenReturn(ActivitiesFactory.
                getActivitiesSkill("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivitiesSkill("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivitiesSkill("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivitiesSkill("activityIdForTesting")).thenReturn(ActivitiesFactory.
                getActivitiesSkill("activityIdForTesting"));

        final HashMap<String, String> countAndJoinMock = new HashMap<>();
        countAndJoinMock.put("count", String.valueOf(volunteerCount));
        countAndJoinMock.put("volJoin", String.valueOf(volunteerStatus));
        //return HashMap using the mocked DAO
        when(activityDao.getActVolCountAndVolJoined(anyString(), anyString())).
                thenReturn(countAndJoinMock).thenReturn(countAndJoinMock).thenReturn(countAndJoinMock)
                .thenReturn(countAndJoinMock).thenReturn(countAndJoinMock);
        //return matched skill count using the mocked DAO
        when(activityDao.skillsMatchedCount(anyString(), anyString())).thenReturn(4).thenReturn(4)
                .thenReturn(4).thenReturn(4).thenReturn(4);
        //Initialize the class to be tested with the injected mock class
        activityService = new ActivityService(activityDao);
        final List<Activity> activitiesResult = activityService.getActivitiesForUserHomePage(
                "sampleVolunteerId", anyInt());
        for (Activity activity : activitiesResult) {
            assertEquals(activity.getVolunteer_count(), volunteerCount);
            assertEquals(activity.getVolunteerstatus(), "yes");
        }
        assertTrue(activitiesResult.containsAll(activitiesFromFactory));
    }

    /**
     * This function is used to test sortActivitiesAccordingToSkillCount function
     */
    @Test
    void testSortActivitiesAccordingToSKillCount(){
        final List<Activity> activitiesFromFactory = ActivitiesFactory.getActivitiesForSortSkillCount();
        //extract each activity and identify the correct order for skill count
        final Activity activity1st = activitiesFromFactory.get(3);
        final Activity activity2nd = activitiesFromFactory.get(4);
        final Activity activity3rd = activitiesFromFactory.get(1);
        final Activity activity4th = activitiesFromFactory.get(0);
        final Activity activity5th = activitiesFromFactory.get(2);
        activityService = new ActivityService(new ActivityDao());
        final List<Activity> sortedList = activityService.sortActivitiesAccordingToSkillCount(activitiesFromFactory);
        //assert if list is sorted
        assertEquals(activity1st, sortedList.get(0));
        assertEquals(activity2nd, sortedList.get(1));
        assertEquals(activity3rd, sortedList.get(2));
        assertEquals(activity4th, sortedList.get(3));
        assertEquals(activity5th, sortedList.get(4));
    }
}
