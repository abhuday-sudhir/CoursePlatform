package com.project.CoursePlatform.controller;

import com.project.CoursePlatform.Dto.CourseSummaryDto;
import com.project.CoursePlatform.Dto.SearchResponseDto;
import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
@Tag(name = "1. Course APIs" , description = "Search courses by id and queries")
public class CourseController {

    @Autowired
    CourseService courseService;

    @Operation(summary = "Search for all courses")
    @GetMapping("courses")
    public ResponseEntity<List<CourseSummaryDto>>getCourses()
    {
        return new ResponseEntity<>(courseService.getCourses(), HttpStatus.OK);
    }

    @Operation(summary = "Search for courses by name or id")
    @GetMapping("courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id)
    {
        return new ResponseEntity<>(courseService.getCourseById(id),HttpStatus.OK);
    }

    @Operation(summary = "Search for all courses related to query")
    @GetMapping("search")
    public ResponseEntity<SearchResponseDto> search(@RequestParam String q) {
        return new ResponseEntity<>(new SearchResponseDto(q,courseService.search(q)),HttpStatus.OK);
    }

}
