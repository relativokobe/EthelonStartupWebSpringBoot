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
import java.util.HashMap;

import static com.example.ethelon.utility.Constants.*;

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
            final HashMap<String, Object> jsonObjectResponse = new HashMap<>();
            jsonObjectResponse.put(Constants.MESSAGE, Constants.EMAIL_ALREADY_EXISTS);
            //Write response data
            writeResponseData(response, jsonObjectResponse);
            return;
        }

        final String name = retrieveStringObject(jsonObjectRequest, "name");
        final String role = retrieveStringObject(jsonObjectRequest, "role");
        final String fcmToken = retrieveStringObject(jsonObjectRequest, "fcm_token");

        if(StringUtils.isAnyEmpty(name, role, fcmToken)){
            //FIXME set response if these are empty
        }
        final String userId = Constants.generateId();
        final Volunteer user = retrieveVolunteerFromReq(jsonObjectRequest, email, fcmToken, userId);
        register(user);
        response.setStatus(HttpServletResponse.SC_OK);
        final HashMap<String, Object> jsonObjectResponse = writeResponseDataForRegister(user.getApiToken(),
                user.getVolunteer_id(), name);
        jsonObjectResponse.put(MESSAGE, SUCCESS);
        writeResponseData(response, jsonObjectResponse);
    }

    /**
     * This function retrieves the volunteer information from the request
     * @param jsonObjectRequest object where the info are stored
     * @param email email of the user
     * @param fcmToken fcm token of the user
     * @return Volunteer object created from the info
     */
    private Volunteer retrieveVolunteerFromReq(final JSONObject jsonObjectRequest, final String email, final String fcmToken,
                                               final String userId){
        final String password = retrieveStringObject(jsonObjectRequest, "password");
        final String name = retrieveStringObject(jsonObjectRequest, "name");
        final String role = retrieveStringObject(jsonObjectRequest, "role");
        final String location = retrieveStringObject(jsonObjectRequest, "location");
        final String imageUrl = retrieveStringObject(jsonObjectRequest, "image_url");
        final int age = retrieveIntegerObject(jsonObjectRequest, "age");
        final String HashedPassword =  HashPasswordUtility.getHashPassword(password);
        final String volunteerId = Constants.generateId();

        //FIXME temporary
        final String apiToken = userId;

        //FIXME there should be a process to know which role
        return new Volunteer(userId, name, email, HashedPassword, role, apiToken, volunteerId,
                location, imageUrl, fcmToken, age, 0);
    }

    /**
     * Function to call service to register user
     * @param user to be registered
     */
    private void register(final Volunteer user){
        userService.insertUserToDb(user);
        ////FIXME there should be a process to know which role
        volunteerService.insertVolunteerToDb(user);
    }

    /**
     * Function to write response data for register
     * @param apiToken api token of the user
     * @param volunteerId id of the volunteer
     * @param name name of the user
     * @return HashMap for the response data
     */
    private HashMap<String, Object> writeResponseDataForRegister(final String apiToken, final String volunteerId,
                                                           final String name){
        final HashMap<String, Object> jsonObjectResponse = new HashMap<>();
        jsonObjectResponse.put("api_token", apiToken);
        jsonObjectResponse.put("volunteer_id", volunteerId);
        jsonObjectResponse.put("name", name);

        return jsonObjectResponse;
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
        final HashMap<String, Object> jsonObjectResponse = new HashMap<>();
        //FIXME STATUS IS OK EVEN THOUGH CREDENTIALS ARE INVALID
        response.setStatus(HttpServletResponse.SC_OK);
        //Credentials did not match any of the records in the DB
        if(user == null){
            jsonObjectResponse.put(Constants.MESSAGE, Constants.INVALID_CREDENTIALS);
        }else{
            jsonObjectResponse.put(Constants.MESSAGE, SUCCESS);
            jsonObjectResponse.put("api_token", user.getApiToken());
            //FIXME
            jsonObjectResponse.put("volunteer_id", ((Volunteer)user).getVolunteer_id());
            jsonObjectResponse.put("name", user.getName());
            jsonObjectResponse.put("image_url", ((Volunteer)user).getImage_url());
        }
        writeResponseData(response, jsonObjectResponse);
    }

    /**
     * '/loginwithfb' is called from client to login user using FB.
     * @param request request from client
     * @param response response to send to client
     */
    @RequestMapping("/loginwithfb")
    public void loginWithFb(final HttpServletRequest request, final HttpServletResponse response){
        final JSONObject jsonObjectRequest = Constants.retrieveDataFromRequest(request);
        //facebook ID is the user ID of users that are using login with facebook
        final String facebookId = retrieveStringObject(jsonObjectRequest, "facebook_id");
        final boolean facebookIdExists = userService.checkIfUserIdExists(facebookId);
        //TODO put some role identification
        final String role = retrieveStringObject(jsonObjectRequest, "role");
        final String fcmToken = retrieveStringObject(jsonObjectRequest, "fcm_token");
        final String message;
        final Volunteer volunteer;
        if(facebookIdExists){
            volunteer = volunteerService.retrieveVolunteerUsingUserId(facebookId);
            volunteerService.fcmToken(volunteer.getVolunteer_id(), fcmToken);
            message = NOT_FIRST_TIME;
        }else{
            final String email = retrieveStringObject(jsonObjectRequest, "email");
            volunteer = retrieveVolunteerFromReq(jsonObjectRequest, email, fcmToken, facebookId);
            //Register user using facebook for the first time
            register(volunteer);
            message = FIRST_TIME;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        final String name = retrieveStringObject(jsonObjectRequest, "name");
        final HashMap<String, Object> jsonObjectResponse = writeResponseDataForRegister(volunteer.getApiToken(),
                volunteer.getVolunteer_id(), name);
        jsonObjectResponse.put(MESSAGE, message);
        writeResponseData(response, jsonObjectResponse);
    }

}
