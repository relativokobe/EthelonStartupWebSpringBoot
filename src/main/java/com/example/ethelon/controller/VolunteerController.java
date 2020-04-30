package com.example.ethelon.controller;

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
import java.util.List;
import static com.example.ethelon.utility.Constants.writeResponseDataArray;
import static com.example.ethelon.utility.Constants.SUCCESS;
import static com.example.ethelon.utility.Constants.writeResponseData;
import static com.example.ethelon.utility.Constants.ALREADY_JOINED;
import static com.example.ethelon.utility.Constants.MESSAGE;
import static com.example.ethelon.utility.Constants.REGISTERED;
import static com.example.ethelon.utility.Constants.NOT_REGISTERED;

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
        final String volunteerId = request.getParameter("volunteer_id");
        final String countString = request.getParameter("count");
        final int count = countString == null ? 0 : Integer.parseInt(countString);
        //FIXME should be Skill, not String
        final List<String> skills = new ArrayList<>();
        //Retrieve skills
        for(int i = 0; i < count; i++){
            skills.add(request.getParameter("params"+i));
        }

        volunteerService.insertVolunteerSkills(volunteerId, skills);
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
        final String volunteerId = request.getParameter("volunteer_id");
        final String activityId = request.getParameter("activity_id");
        final JSONObject jsonObject = new JSONObject();

        //check if volunteer already joined activity
        if(volunteerService.volunteerJoinedActivity(volunteerId, activityId)){
            jsonObject.put(MESSAGE, ALREADY_JOINED);
        }else{
            volunteerService.joinActivity(volunteerId, activityId);
            jsonObject.put(MESSAGE, SUCCESS);
        }

        writeResponseData(response, jsonObject);
    }

    /**
     * '/volunteerstorate' is called from client retrieve the volunteers to be rated by current volunteer
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/groupmatestorate")
    public void groupmatestorate(final HttpServletRequest request, final HttpServletResponse response){
        final List<VolunteerToRate> volunteers = volunteerService.retrieveVolunteersToRate(request.getParameter(
                "volunteer_id"), request.getParameter("activity_id"));
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
        final JSONObject jsonObject = new JSONObject();
        final String resultMessage = volunteerService.volunteerJoinedActivity(request.getParameter("volunteer_id"),
                request.getParameter("activity_id")) ? REGISTERED : NOT_REGISTERED;
        jsonObject.put(MESSAGE, resultMessage);
        writeResponseData(response, jsonObject);
    }

    /**
     * '/volunteerprofile' is called from client to retrieve the details of the volunteer
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/volunteerprofile")
    public void volunteerprofile(final HttpServletRequest request, final HttpServletResponse response){
        final List<VolunteerBadgesInfoResponse> info = volunteerService.retrieveVolunteerProfile
                (request.getParameter("volunteer_id"));
        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String jsonArray = gson.toJson(info);
        writeResponseDataArray(response, jsonArray);
    }
}
