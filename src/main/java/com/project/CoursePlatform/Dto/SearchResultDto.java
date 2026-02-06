package com.project.CoursePlatform.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {
    private String courseId;
    private String courseTitle;
    List<SearchMatchDto> matches;
}
