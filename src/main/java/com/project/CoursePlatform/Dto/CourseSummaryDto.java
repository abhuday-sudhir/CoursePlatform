package com.project.CoursePlatform.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSummaryDto {
    private String id;
    private String title;
    private String description;
    private int topicCount;
    private int subtopicCount;
}
