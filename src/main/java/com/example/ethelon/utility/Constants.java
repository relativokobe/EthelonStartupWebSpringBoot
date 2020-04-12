package com.example.ethelon.utility;

import com.example.ethelon.model.Skill;
import org.apache.commons.lang3.RandomStringUtils;

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
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final int ZERO = 0;
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
}
