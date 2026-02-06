package com.project.CoursePlatform.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponseDto {
    Long enrollmentId;
    String courseId;
    String courseTitle;
    int totalSubtopics;
    int completedSubtopics;
    double completionPercentage;
    List<CompletedItemDto> completedItems;

}
