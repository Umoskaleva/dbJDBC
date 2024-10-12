package ru.specialist.dao;

import java.util.List;

public interface CourseDao {

    Course findById(int id);
    List<Course> findAll();
    List<Course> findByTitle(String title);

    // CRAD
    void insert(Course course);
    void update(Course course);
    void delete(int id);

}
