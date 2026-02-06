package com.project.CoursePlatform.controller;

import com.project.CoursePlatform.Dto.ProgressResponseDto;
import com.project.CoursePlatform.service.ViewProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/enrollments")
@Tag(name="5. View Progress APIs" , description = "Check your progress")
public class ViewProgressController {
    @Autowired
    ViewProgressService viewProgressService;

    @Operation(summary = "View progress for a specific enrollment")
    @PostMapping("/{enrollmentId}/progress")
    public ProgressResponseDto view(@PathVariable long enrollmentId)
    {
        return viewProgressService.viewProgress(enrollmentId);
    }
}
