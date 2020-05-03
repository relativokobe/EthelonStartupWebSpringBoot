package com.example.ethelon.utility;

import com.example.ethelon.model.Skill;
import org.apache.commons.lang3.RandomStringUtils;
import org.cloudinary.json.JSONException;
import org.cloudinary.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Constants to be used for the project
 */
public class Constants {
    public static final String MESSAGE = "message";
    public static final String EMAIL_ALREADY_EXISTS = "email already exists";
    public static final String SUCCESS = "Success";
    public static final String NOTHING = "Nothing";
    public static final String ALREADY_JOINED = "Already Joined";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int DEFAULT_VOLUNTEER_POINTS = ZERO;
    public static final String REGISTERED = "Registered";
    public static final String NOT_REGISTERED = "Not Registered";
    /**
     * This function is used to retrieve skills
     * FIXME Add a config file that could dynamically add Skills
     * @return list of skills
     */
    public static List<Skill> getSkills(){
        final List<Skill> skills = new ArrayList<>();
        skills.add(Skill.Environment);
        skills.add(Skill.Education);
        skills.add(Skill.Sports);
        skills.add(Skill.Arts);
        skills.add(Skill.Medical);
        skills.add(Skill.Culinary);
        skills.add(Skill.Livelihood);
        skills.add(Skill.Charity);

        return skills;
    }

    /**
     * Function to generate ID
     * @return ID generated
     */
    public static String generateId(){
        return RandomStringUtils.random(7, true, true);
    }

    /**
     * This function is used to write data to Response body using JSON object
     * @param response where the JSON object data will be written to
     * @param jsonObject the data to be written to response
     */
    public static void writeResponseData(final HttpServletResponse response, final JSONObject jsonObject){
        try {
            jsonObject.write(response.getWriter());
        } catch (final IOException e) {
            System.out.println("Error writing to Response. " + e.toString());
        }
    }

    /**
     * This function is used to write data to response body using String jsonArray
     * @param response where the String json array will be written to
     * @param jsonArray the String json array to be written to
     */
    public static void writeResponseDataArray(final HttpServletResponse response, final String jsonArray){
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write(jsonArray);
            response.getWriter().flush();
        } catch (IOException e) {
            System.out.println("Error writing to Response. " + e.toString());
        }
    }

    /**
     * This function returns current timme now String
     * @return String value of current time
     */
    public static String getCurrentTimeInString(){
        return String.valueOf(LocalTime.now());
    }

    /**
     * This function returns request data in a form of JSONObject
     * @param request request where the data is stored
     * @return JSONObject
     */
    public static JSONObject retrieveDataFromRequest(final HttpServletRequest request){
        JSONObject jsonObject = null;
        final StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            final BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        try {
            jsonObject =  new JSONObject(jb.toString());
        } catch (final JSONException e) {
            System.out.println("error");
        }
        return jsonObject;
    }

    /**
     * This function retrieves the String from object json
     * @param json json object
     * @param property property to be retrieved
     * @return String value from property
     */
    public static String retrieveStringObject(final JSONObject json, final String property){
        try{
            final Object object = json.get(property);
            return object == null ? null : String.valueOf(object);
        }catch (final Exception e){
            return null;
        }
    }

    /**
     * This function retrieves the int from object json
     * @param json json object
     * @param property property to be retrieved
     * @return String value from property
     */
    public static int retrieveIntegerObject(final JSONObject json, final String property){
        final Object object = json.get(property);
        return object == null ? 0 : Integer.parseInt(String.valueOf(object));
    }
}
