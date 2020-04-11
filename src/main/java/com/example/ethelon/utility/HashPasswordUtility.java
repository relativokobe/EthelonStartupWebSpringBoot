package com.example.ethelon.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This Utility class is used for password operations
 * @author Kobe Kyle Relatvo
 */
public class HashPasswordUtility {
    /**
     * This function is used to Encode the password
     * @param password to be encoded
     * @return encoded password
     */
    public static String getHashPassword(final String password){
      return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * This function matches the encoded password and the raw password
     * @param password raw password
     * @param hashedPassword encoded password
     * @return boolean value if password and encoded password are a match
     */
    public static boolean passwordAndHashMatcher(final String password, final String hashedPassword){
        return new BCryptPasswordEncoder().matches(password, hashedPassword);
    }
}
