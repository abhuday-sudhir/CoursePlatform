package com.project.CoursePlatform.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedItemDto {
    String subtopicId;
    String subtopicTitle;
    Instant completedAt;
}
