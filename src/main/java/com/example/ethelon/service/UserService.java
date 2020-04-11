package com.example.ethelon.service;

import com.example.ethelon.dao.UserDAO;
import com.example.ethelon.model.User;
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

}
