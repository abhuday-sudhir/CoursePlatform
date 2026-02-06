package com.project.CoursePlatform.controller;

import com.project.CoursePlatform.Dto.EnrollmentResponseDto;
import com.project.CoursePlatform.entity.Enrollment;
import com.project.CoursePlatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/courses")
@Tag(name = "3. Enrollment APIs" , description = "Enroll yourself in courses")
public class EnrollmentController {
    @Autowired
    EnrollmentService enrollmentService;

    @Operation(summary = "Enroll in a course")
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponseDto> enroll(@PathVariable String courseId) {
        return new ResponseEntity<>(enrollmentService.enroll(courseId), HttpStatus.CREATED);
    }
}
