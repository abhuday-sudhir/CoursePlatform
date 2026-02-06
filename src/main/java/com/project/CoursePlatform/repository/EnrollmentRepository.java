package com.project.CoursePlatform.repository;

import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.entity.Enrollment;
import com.project.CoursePlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserAndCourse(User user, Course course);
    Optional<Enrollment> findByIdAndUser(Long id, User user);
    List<Enrollment> findByUser(User user);
}
