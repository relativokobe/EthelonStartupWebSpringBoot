package com.example.ethelon.dao;

import com.example.ethelon.model.User;
import com.example.ethelon.model.Volunteer;
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

    /**
     * This function retrieves User from DB to login based on email
     * //FIXME Volunteer is being retrieved but there should be a role identifier to query other roles in the future
     * @param email email of the user
     * @return User details. Returns null if user not found
     */
    public Volunteer login(final String email){
        final Object[] args = new Object[]{email};
        final String query = "SELECT users.name, users.password, users.api_token, volunteers.volunteer_id, volunteers.image_url " +
                "FROM users INNER JOIN volunteers ON users.user_id = volunteers.user_id WHERE users.email = ? ";

        return jdbcTemplate.query(query, args, rs ->{
            Volunteer user = null;
            if(rs.next()){
                user = new Volunteer();
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setApiToken(rs.getString("api_token"));
                user.setVolunteerId(rs.getString("volunteer_id"));
                user.setImageUrl(rs.getString("image_url"));
            }
            return user;
        });
    }

}
