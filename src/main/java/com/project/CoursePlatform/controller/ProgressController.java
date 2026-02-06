package com.project.CoursePlatform.controller;

import com.project.CoursePlatform.Dto.ProgressCompleteDto;
import com.project.CoursePlatform.entity.SubtopicProgress;
import com.project.CoursePlatform.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subtopics")
@Tag(name="4. Progress Tracking APIs", description = "Mark your subtopic as completed")
public class ProgressController {

    @Autowired
    ProgressService progressService;

    @Operation(summary = "Mark subtopic as complete")
    @PostMapping("/{subtopicId}/complete")
    public ProgressCompleteDto complete(@PathVariable String subtopicId) {
        return progressService.completeSubtopic(subtopicId);
    }
}
