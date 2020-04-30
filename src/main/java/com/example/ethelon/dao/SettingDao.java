package com.example.ethelon.dao;

import com.example.ethelon.model.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * DAO of settings
 * @author Kobe Kyle Relativo
 */
@Repository
public class SettingDao {
    /**
     * Used to connect to Database and do Queries
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Setting getSetting(){
        final String query = "SELECT * FROM settings";
        return jdbcTemplate.query(query, resultSet -> {
            Setting setting = null;
            if(resultSet.next()){
                setting = new Setting(resultSet.getInt("id"), resultSet.getInt("activityPresetPoints"),
                        resultSet.getInt("activityHoursRenderedMultiplier"), resultSet.getInt("activityPointsPerRating"),
                        resultSet.getInt("newbieGauge"), resultSet.getInt("explorerGauge"),
                        resultSet.getInt("expertGauge"), resultSet.getInt("legendGauge"),
                        resultSet.getInt("agePercentage"), resultSet.getInt("pointPercentage"),
                        resultSet.getInt("ageTotal"), resultSet.getInt("pointTotal"));
            }
            return setting;
        });
    }
}
