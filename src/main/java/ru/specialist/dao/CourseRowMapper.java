package ru.specialist.dao;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseRowMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Course(rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("Length"),
                rs.getString("description"));
    }
}
