package com.example.ethelon.dao;

import com.example.ethelon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * DAO for user
 * @author Kobe Kyle Relativo
 */
@Repository
public class UserDAO {
    /**
     * Used to connect to Database and do Queries
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * This function checks if email to be registered already exists in DB or not
     *
     * @param email email of the user to be used for registering
     * @return boolean if email exists
     */
    public boolean checkIfEmailAlreadyExists(final String email){
         final Object[] args = new Object[]{email};
         final Integer half =  jdbcTemplate.query("SELECT COUNT(*) as email_count FROM users where email = ? ", args, resultSet ->
                     resultSet.next() ? resultSet.getInt("email_count") : 0);
         if(half == null){
             return false;
         }else{
             return half > 0;
         }
    }

    /**
     * Function to insert user to DB
     *
     * @param user user to be inserted to DB
     */
    public void insertUserToDb(final User user){
        final String query = "INSERT INTO users(user_id, name, email, password, role, api_token) " +
                "VALUES (?, ?, ?, ?, ?, ?);" ;
        final Object[] args = new Object[]{user.getUserId(), user.getName(), user.getEmail(), user.getPassword(),
                user.getRole(), user.getApiToken()};
        jdbcTemplate.update(query, args);
    }

}
