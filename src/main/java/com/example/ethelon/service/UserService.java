package com.example.ethelon.service;

import com.example.ethelon.dao.UserDAO;
import com.example.ethelon.model.User;
import com.example.ethelon.model.Volunteer;
import com.example.ethelon.utility.HashPasswordUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for user. Business logic for User interactions are placed here
 * @author Kobe Kyle Relativo
 */
@Service
public class UserService {
    /**
     * Dao for user
     */
    @Autowired
    private UserDAO userDAO;

    /**
     * Constructor for Unit Testing
     * @param userDAO mocked or spied userDao for Unit Testng
     */
    public UserService(final UserDAO userDAO){
        this.userDAO = userDAO;
    }

    /**
     * Service that calls DAO to check if email already exists in DB
     * @param email to be checked
     * @return boolean if email exists or not
     */
    public boolean checkIfEmailAlreadyExists(final String email){
        return userDAO.checkIfEmailAlreadyExists(email);
    }

    /**
     * Service that calls DAO to insert User
     * @param user User to be inserted
     */
    public void insertUserToDb(final User user){
        userDAO.insertUserToDb(user);
    }

    /**
     * Function that handles user login
     * @param email email of the user
     * @param password password of the user
     * FIXME JSONObject should be converted to a model for login and register response
     * @return User retrieved from DB. Returns null if no match.
     */
    public User login(final String email, final String password){
        Volunteer user = userDAO.login(email);
        //User exists
        if(user != null){
            //password did not much
            if(!HashPasswordUtility.passwordAndHashMatcher(password, user.getPassword())){
                user = null;
            }
        }
        return user;
    }

    /**
     * Service that calls DAO to check if user ID existss
     * @param userId ID of the user to be checked
     * @return boolean if user ID exists or not
     */
    public boolean checkIfUserIdExists(final String userId){
        return userDAO.checkIfUserIdExists(userId);
    }

}
