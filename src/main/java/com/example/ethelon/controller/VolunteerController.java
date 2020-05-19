package com.example.ethelon.controller;

import com.example.ethelon.model.LeaderBoardVolunteer;
import com.example.ethelon.model.VolunteerBadgesInfoResponse;
import com.example.ethelon.model.VolunteerToRate;
import com.example.ethelon.service.VolunteerService;
import com.example.ethelon.utility.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//FIXME
import static com.example.ethelon.utility.Constants.*;
import static com.example.ethelon.utility.Constants.retrieveStringObject;

/**
 * Controller to handle requests for Volunteer interactions
 * @author Kobe Kyle Relativo
 */
@RequestMapping("/api")
@Controller
public class VolunteerController {
    /**
     * Service for vollunteer
     */
    @Autowired
    private VolunteerService volunteerService;

    /**
     * This constructor is for Unit Testing
     */
    public VolunteerController(final VolunteerService mockedService){
        volunteerService = mockedService;
    }

    /**
     * '/volunteerskills' is called from client to save volunteer skills
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/volunteerskills")
    public void volunteerskills(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String volunteerId = retrieveStringObject(jsonObjectRequest, "volunteer_id");
        final String countString = retrieveStringObject(jsonObjectRequest, "count");
        final int count = countString == null ? 0 : Integer.parseInt(countString);
        //FIXME should be Skill, not String
        final List<String> skills = new ArrayList<>();

        if(count > 0){
            //Retrieve skills
            for(int i = 0; i < count; i++){
                skills.add(retrieveStringObject(jsonObjectRequest, "params"+i));
            }
            volunteerService.insertVolunteerSkills(volunteerId, skills);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(Constants.SUCCESS);
    }

    /**
     * '/joinactivity' is called from client to make the volunteer join the activity
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/joinactivity")
    public void joinactivity(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String volunteerId = retrieveStringObject(jsonObjectRequest, "volunteer_id");
        final String activityId = retrieveStringObject(jsonObjectRequest, "activity_id");
        final HashMap<String, Object> jsonObjectResponse = new HashMap<>();

        //check if volunteer already joined activity
        if(volunteerService.volunteerJoinedActivity(volunteerId, activityId)){
            jsonObjectResponse.put(MESSAGE, ALREADY_JOINED);
        }else{
            volunteerService.joinActivity(volunteerId, activityId);
            jsonObjectResponse.put(MESSAGE, SUCCESS);
        }

        writeResponseData(response, jsonObjectResponse);
    }

    /**
     * '/volunteerstorate' is called from client retrieve the volunteers to be rated by current volunteer
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/groupmatestorate")
    public void groupmatestorate(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String volunteerId = retrieveStringObject(jsonObjectRequest, "volunteer_id");
        final String activityId = retrieveStringObject(jsonObjectRequest, "activity_id");
        final List<VolunteerToRate> volunteers = volunteerService.retrieveVolunteersToRate(volunteerId, activityId);
        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String jsonArray = gson.toJson(volunteers);
        writeResponseDataArray(response, jsonArray);
    }

    /**
     * '/checkIfAlreadyAttended' is called from client to check if volunteer already attended the activity
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/checkIfAlreadyAttended")
    public void checkIfAlreadyAttended(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String volunteerId = retrieveStringObject(jsonObjectRequest, "volunteer_id");
        final String activityId = retrieveStringObject(jsonObjectRequest, "activity_id");
        final HashMap<String, Object> jsonObjectResponse = new HashMap<>();
        final String resultMessage = volunteerService.volunteerJoinedActivity(volunteerId, activityId)
                ? REGISTERED : NOT_REGISTERED;
        jsonObjectResponse.put(MESSAGE, resultMessage);
        writeResponseData(response, jsonObjectResponse);
    }

    /**
     * '/volunteerprofile' is called from client to retrieve the details of the volunteer
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/volunteerprofile")
    public void volunteerprofile(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String volunteerId = retrieveStringObject(jsonObjectRequest, "volunteer_id");
        final List<VolunteerBadgesInfoResponse> info = volunteerService.retrieveVolunteerProfile(volunteerId);
        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String jsonArray = gson.toJson(info);
        writeResponseDataArray(response, jsonArray);
    }

    /**
     * '/leaderboard' is called from client to retrieve the list of volunteer for leaderboard
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/leaderboard")
    public void leaderboard(final HttpServletRequest request, final HttpServletResponse response){
        final List<LeaderBoardVolunteer> volunteers = volunteerService.getVolunteersForLeaderBoard();
        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String jsonArray = gson.toJson(volunteers);
        writeResponseDataArray(response, jsonArray);
    }

    /**
     * '/fcm_token' is called from client to update the FCM token of the user
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/fcm_token")
    public void fcmToken(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String volunteerId = retrieveStringObject(jsonObjectRequest, "volunteer_id");
        final String fcmToken = retrieveStringObject(jsonObjectRequest, "fcm_token");
        volunteerService.fcmToken(volunteerId, fcmToken);
        final HashMap<String, Object> jsonObjectResponse = new HashMap<>();
        jsonObjectResponse.put(MESSAGE, SUCCESS);
        writeResponseData(response, jsonObjectResponse);
    }
}
