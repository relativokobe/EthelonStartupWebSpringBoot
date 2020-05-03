package com.example.ethelon.controller;

import com.example.ethelon.model.User;
import com.example.ethelon.model.Volunteer;
import com.example.ethelon.service.UserService;
import com.example.ethelon.service.VolunteerService;
import com.example.ethelon.utility.Constants;
import com.example.ethelon.utility.HashPasswordUtility;
import org.apache.commons.lang3.StringUtils;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//FIXME
import static com.example.ethelon.utility.Constants.*;
import static com.example.ethelon.utility.Constants.retrieveStringObject;

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
        final JSONObject jsonObjectRequest = Constants.retrieveDataFromRequest(request);
        final String email = retrieveStringObject(jsonObjectRequest, "email");
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

        final String password = retrieveStringObject(jsonObjectRequest, "password");
        final String name = retrieveStringObject(jsonObjectRequest, "name");
        final String role = retrieveStringObject(jsonObjectRequest, "role");
        final String fcmToken = retrieveStringObject(jsonObjectRequest, "fcm_token");
        final String location = retrieveStringObject(jsonObjectRequest, "location");
        final String imageUrl = retrieveStringObject(jsonObjectRequest, "image_url");
        final int age = retrieveIntegerObject(jsonObjectRequest, "age");
        final String HashedPassword =  HashPasswordUtility.getHashPassword(password);
        final String volunteerId = Constants.generateId();
        final String userId = Constants.generateId();
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
        final JSONObject jsonObjectResponse = new JSONObject();
        jsonObjectResponse.put("api_token", apiToken);
        jsonObjectResponse.put("volunteer_id", volunteerId);
        jsonObjectResponse.put("name", name);
        jsonObjectResponse.put(Constants.MESSAGE, SUCCESS);
        writeResponseData(response, jsonObjectResponse);
    }

    /**
     * '/login' is called from client to login user.
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/login")
    public void login(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = retrieveDataFromRequest(request);
        final String email = retrieveStringObject(jsonObjectRequest, "email");
        final String password = retrieveStringObject(jsonObjectRequest, "password");

        if(StringUtils.isAnyEmpty(email, password)){
            //FIXME DO SOMETHING
        }

        final User user = userService.login(email, password);
        final JSONObject jsonObject = new JSONObject();
        //FIXME STATUS IS OK EVEN THOUGH CREDENTIALS ARE INVALID
        response.setStatus(HttpServletResponse.SC_OK);
        //Credentials did not match any of the records in the DB
        if(user == null){
            jsonObject.put(Constants.MESSAGE, Constants.INVALID_CREDENTIALS);
        }else{
            jsonObject.put(Constants.MESSAGE, SUCCESS);
            jsonObject.put("api_token", user.getApiToken());
            //FIXME
            jsonObject.put("volunteer_id", ((Volunteer)user).getVolunteer_id());
            jsonObject.put("name", user.getName());
            jsonObject.put("image_url", ((Volunteer)user).getImage_url());
        }
        writeResponseData(response, jsonObject);
    }

}
