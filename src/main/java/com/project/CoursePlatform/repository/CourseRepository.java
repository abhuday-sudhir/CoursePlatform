package com.project.CoursePlatform.repository;

import com.project.CoursePlatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
