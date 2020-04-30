package com.example.ethelon.dao;

import com.example.ethelon.model.Badge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao of skill
 * @author Kobe Kyle Relativo
 */
@Repository
public class SkillDao {
    /**
     * Used to connect to Database and do Queries
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * This function retrieves the badges according to skill
     * @param skill the skill used for retrieving the badges
     * @return list of badges
     */
    public List<Badge> getBadgesAccordingToSkill(final String skill){
        final Object[] args = new Object[]{skill};
        final String query = "SELECT * FROM badges WHERE skill = ?";
        return jdbcTemplate.query(query, args, resultSet -> {
            final List<Badge> badges = new ArrayList<>();
            while(resultSet.next()){
                final Badge badge = new Badge();
                badges.add(badge);
            }
            return badges;
        });
    }
}
