package com.project.CoursePlatform.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMatchDto {
    private String type;
    private String topicTitle;
    private String subtopicId;
    private String subtopicTitle;
    private String snippet;
}
