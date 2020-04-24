package com.example.ethelon.controller;

import com.example.ethelon.model.Activity;
import com.example.ethelon.service.ActivityService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    public void getAllActivitiesNotDone(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String offsetString = request.getParameter("offset");
        final List<Activity> activities = activityService.getActivitiesForUserHomePage(request.
                getParameter("volunteer_id"), offsetString == null ? 0 : Integer.parseInt(offsetString));

        final String jsonArray = new Gson().toJson(activities);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(jsonArray);
        response.getWriter().flush();
    }
}
