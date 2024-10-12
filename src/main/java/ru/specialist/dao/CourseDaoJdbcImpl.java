package ru.specialist.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository("courseDao") // аннотация репозиторий
public class CourseDaoJdbcImpl implements CourseDao { // мы реализовали репозиторий

    private static final String SQL_SELECT_COURSE =
            "SELECT id, title, length, description FROM courses";


    private static final String SQL_SELECT_COURSE_BY_ID =
            "SELECT id, title, length, description FROM courses WHERE id = ?";

    private static final String SQL_DELETE_COURSE_BY_ID =
            "DELETE FROM courses WHERE id = ?";

    private static final String SQL_INSERT_COURSE =
            "INSERT INTO courses (title, length, description) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE_COURSE =
            "UPDATE courses SET title = ?, length = ?, description =? WHERE id = ?";

    private static final String SQL_SELECT_COURSE_BY_TITLE =
            SQL_SELECT_COURSE + " WHERE title LIKE ?";


    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private CourseRowMapper courseRowMapper;

    @Override
    public Course findById(int id) {
        return getJdbcTemplate().queryForObject(
                SQL_SELECT_COURSE_BY_ID,
                courseRowMapper, id);
    }

    @Override
    public List<Course> findAll() { // происходит SQL запрос к базе и получает коллекцию строк
//        List<Map<String, Object>> rows =
//                getJdbcTemplate().queryForList(SQL_SELECT_COURSE);
//        List<Course> courses = new ArrayList<>();
//        for (Map<String, Object> row : rows) // возвращает коллекцию обьектов
//            courses.add(new Course((int) row.get("id"),
//                    (String) row.get("title"),
//                    (int) row.get("Length"),
//                    (String) row.get("description")));
//        return courses;

//        return getJdbcTemplate().query(SQL_SELECT_COURSE, courseRowMapper);
        return getJdbcTemplate().query(SQL_SELECT_COURSE, new BeanPropertyRowMapper(Course.class)); //этот вариант работает если названия свойств в классе (Course) совпадают с колонками в таблице
    }

    @Override
    public List<Course> findByTitle(String title) {
        return getJdbcTemplate().query(SQL_SELECT_COURSE_BY_TITLE, courseRowMapper, "%" + title + "%");
    }

    @Override
    public void insert(Course course) {
        PreparedStatementCreatorFactory f =
                new PreparedStatementCreatorFactory(SQL_INSERT_COURSE,
                        Types.NVARCHAR, Types.INTEGER, Types.NVARCHAR);

        f.setGeneratedKeysColumnNames("d");
        KeyHolder kh = new GeneratedKeyHolder();

        getJdbcTemplate().update(
                f.newPreparedStatementCreator(new Object[]{
                        course.getTitle(), course.getLength(), course.getDescription()}), kh);
        course.setId(kh.getKey().intValue());
    }


    @Override
    public void update(Course course) {
        getJdbcTemplate().update(SQL_UPDATE_COURSE,
                course.getTitle(), course.getLength(),
                course.getDescription(), course.getId());
    }

    @Override
    public void delete(int id) {
        getJdbcTemplate().update(SQL_DELETE_COURSE_BY_ID, id);

    }
}
