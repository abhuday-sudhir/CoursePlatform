package com.project.CoursePlatform.service;

import com.project.CoursePlatform.Dto.EnrollmentResponseDto;
import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.entity.Enrollment;
import com.project.CoursePlatform.entity.User;
import com.project.CoursePlatform.exception.ConflictException;
import com.project.CoursePlatform.exception.ResourceNotFoundException;
import com.project.CoursePlatform.repository.CourseRepository;
import com.project.CoursePlatform.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CurrentUserService currentUserService;

    public EnrollmentResponseDto enroll(String courseId) {

        User user = currentUserService.getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new ConflictException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);

        Enrollment saved = enrollmentRepository.save(enrollment);

        return new EnrollmentResponseDto(
                saved.getId(),
                course.getId(),
                course.getTitle(),
                saved.getEnrolledAt()
        );
    }
}
