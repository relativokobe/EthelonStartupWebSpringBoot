package com.example.ethelon.controller;

import com.example.ethelon.model.Volunteer;
import com.example.ethelon.service.UserService;
import com.example.ethelon.service.VolunteerService;
import com.example.ethelon.utility.Constants;
import com.example.ethelon.utility.HashPasswordUtility;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.ethelon.utility.Constants.SUCCESS;

/**
 * Controller to handle Requests for User interactions
 * FIXME Needs validation
 * @author Kobe Kyle Relativo
 */
@RequestMapping("/api")
@Controller
public class UserController {
    /**
     * Service for User
     */
    @Autowired
    final private UserService userService;
    /**
     * Service for Volunteer (only for inserting)
     */
    @Autowired
    private VolunteerService volunteerService;

    /**
     * This constructor is used for Unit Testing
     * @param userService mocked or spied service
     */
    public UserController(final UserService userService){
        this.userService = userService;
    }

    /**
     * '/register' is called from client to register user and its role.
     * FIXME Lacks validation
     * @param request request from client
     * @param response response to send to client
     */
    @PostMapping("/register")
    public void register(final HttpServletRequest request, final HttpServletResponse response) {
        final String email = request.getParameter("email");
        if (StringUtils.isAnyEmpty(email)) {
            //Set response
        }
        //Email used by user is already registered
        if (userService.checkIfEmailAlreadyExists(email)) {
            //FIXME Response status should not be OK
            response.setStatus(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED);
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.MESSAGE, Constants.EMAIL_ALREADY_EXISTS);
            //Write response data
            writeResponseData(response, jsonObject);
            return;
        }

        final String password = request.getParameter("password");
        final String name = request.getParameter("name");
        final String role = request.getParameter("role");
        final String fcmToken = request.getParameter("fcm_token");
        final String location = request.getParameter("location");
        final String imageUrl = request.getParameter("image_url");
        final String userId = Constants.generateId();
        final int age = Integer.parseInt(request.getParameter("age"));
        final String HashedPassword =  HashPasswordUtility.getHashPassword(password);
        final String volunteerId = Constants.generateId();
        //FIXME temporary
        final String apiToken = userId;
        if(StringUtils.isAnyEmpty(name, role, fcmToken)){
            //FIXME set response if these are empty
        }

        //FIXME there should be a process to know which role
        final Volunteer user = new Volunteer(userId, name, email, HashedPassword, role, apiToken, volunteerId,
                location, imageUrl, fcmToken, age, 0);

        userService.insertUserToDb(user);
        ////FIXME there should be a process to know which role
        volunteerService.insertVolunteerToDb(user);

        //Prepare response.
        response.setStatus(HttpServletResponse.SC_OK);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("api_token", apiToken);
        jsonObject.put("volunteer_id", volunteerId);
        jsonObject.put("name", name);
        jsonObject.put(Constants.MESSAGE, SUCCESS);
        writeResponseData(response, jsonObject);
    }

    /**
     * This function is used to write data to Response body using JSON object
     * @param response where the JSON object data will be written to
     * @param jsonObject the data to be written to response
     */
    private void writeResponseData(final HttpServletResponse response, final JSONObject jsonObject){
        try {
            jsonObject.write(response.getWriter());
        } catch (final IOException e) {
            System.out.println("Error writing to Response. " + e.toString());
        }
    }

}
