package com.example.ethelon.utility;

import com.example.ethelon.model.Skill;
import org.apache.commons.lang3.RandomStringUtils;
import org.cloudinary.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
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
     * This function returns current timme now String
     * @return String value of current time
     */
    public static String getCurrentTimeInString(){
        return String.valueOf(LocalTime.now());
    }
}
