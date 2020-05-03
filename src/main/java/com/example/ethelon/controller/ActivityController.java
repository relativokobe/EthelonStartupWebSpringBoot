package com.example.ethelon.controller;

import com.example.ethelon.model.Activity;
import com.example.ethelon.model.ActivityCriteria;
import com.example.ethelon.model.Volunteer;
import com.example.ethelon.service.ActivityService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//FIXME
import static com.example.ethelon.utility.Constants.*;
import static com.example.ethelon.utility.Constants.retrieveStringObject;

/**
 * Controller to handle Requests for Activities interactions
 * FIXME Needs validation
 * @author Kobe Kyle Relativo
 */
@RequestMapping("/api")
@Controller
public class ActivityController {
    /**
     * Service for activity
     */
    @Autowired
    private ActivityService activityService;

    /**
     * This constructor is for Unit Testing
     */
    public ActivityController(final ActivityService mockedService){
        activityService = mockedService;
    }

    /**
     * '/getallactivities' is called from client to retrieve activities not done.
     * FIXME Lacks validation
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/getallactivities")
    public void getAllActivitiesNotDone(final HttpServletRequest request, final HttpServletResponse response) {
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);

        final String offsetString = retrieveStringObject(jsonObjectRequest, "offset");
        final String volunteerId = retrieveStringObject(jsonObjectRequest, "volunteer_id");
        final List<Activity> activities = activityService.getActivitiesForUserHomePage(volunteerId,
                offsetString == null ? 0 : Integer.parseInt(offsetString));

        final String jsonArray = new Gson().toJson(activities);
        writeResponseDataArray(response, jsonArray);
    }

    /**
     * '/activitycriteria' is called from client to retrieve criteria of an activity.
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/activitycriteria")
    public void activitycriteria(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String activityId = retrieveStringObject(jsonObjectRequest, "activity_id");
        final List<ActivityCriteria> activityCriteriaList = activityService.getActivityCriteria(activityId);

        final String jsonArray = new Gson().toJson(activityCriteriaList);
        writeResponseDataArray(response, jsonArray);
    }

    /**
     * '/activitygetvolunteersbefore' is called from client to retrieve volunteers before activity starts
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/activitygetvolunteersbefore")
    public void activitygetvolunteersbefore(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String activityId = retrieveStringObject(jsonObjectRequest, "activity_id");
        final List<Volunteer> volunteers = activityService.getVolunteersBeforeActStarts(activityId);
        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String jsonArray = gson.toJson(volunteers);
        writeResponseDataArray(response, jsonArray);
    }

    /**
     * '/activitygetvolunteersafter' is called from client to retrieve volunteers after activity starts
     * FIXME Has the same implementation with '/activitygetvolunteersbefore'
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/activitygetvolunteersafter")
    public void activitygetvolunteersafter(final HttpServletRequest request, final HttpServletResponse response){
        activitygetvolunteersbefore(request, response);
    }
}
