package com.example.ethelon.controller;

import com.example.ethelon.service.VolunteerService;
import com.example.ethelon.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
