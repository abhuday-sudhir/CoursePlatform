package com.project.CoursePlatform.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDto {

    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private Instant enrolledAt;
}

