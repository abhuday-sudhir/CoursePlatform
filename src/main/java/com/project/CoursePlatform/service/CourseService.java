package com.project.CoursePlatform.service;

import com.project.CoursePlatform.Dto.CourseSummaryDto;
import com.project.CoursePlatform.Dto.SearchMatchDto;
import com.project.CoursePlatform.Dto.SearchResultDto;
import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.exception.ResourceNotFoundException;
import com.project.CoursePlatform.repository.CourseRepository;
import com.project.CoursePlatform.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SearchRepository searchRepository;

    public List<CourseSummaryDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseSummaryDto> courseSummaryDtos=new ArrayList<>();
        for(Course course:courses)
        {
            CourseSummaryDto courseSummaryDto=new CourseSummaryDto();
            courseSummaryDto.setId(course.getId());
            courseSummaryDto.setTitle(course.getTitle());
            courseSummaryDto.setDescription(course.getDescription());
            courseSummaryDto.setTopicCount(course.getTopics().size());
            courseSummaryDto.setSubtopicCount(course.getTopics()
                    .stream()
                    .mapToInt(t -> t.getSubtopics().size())
                    .sum()
            );
            courseSummaryDtos.add(courseSummaryDto);
        }
        return courseSummaryDtos;
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Course with id '" + id + "' does not exist"
                        )
                );
    }

    public List<SearchResultDto> search(String query) {

        List<Object[]> rows = searchRepository.search(query);
        Map<String, SearchResultDto> resultMap = new LinkedHashMap<>();

        for (Object[] row : rows) {

            String courseId = (String) row[0];
            String courseTitle = (String) row[1];
            String topicTitle = (String) row[2];
            String subtopicId = (String) row[3];
            String subtopicTitle = (String) row[4];
            String type = (String) row[5];
            Float rank = ((Number) row[6]).floatValue();

            String snippet = (String) row[7];

            snippet = cleanSnippet(snippet);

            resultMap.putIfAbsent(courseId,
                    new SearchResultDto(courseId, courseTitle, new ArrayList<>()));

            resultMap.get(courseId).getMatches()
                    .add(new SearchMatchDto(type, topicTitle, subtopicId, subtopicTitle, snippet));
        }

        return new ArrayList<>(resultMap.values());
    }

    private String cleanSnippet(String snippet) {
        if (snippet == null) return "";

        return snippet
                .replaceAll("\\*\\*", "")         // remove bold markdown
                .replaceAll("#+", "")            // remove headers
                .replaceAll("`", "")             // remove code ticks
                .replaceAll("\\\\n", " ")        // remove escaped newlines
                .replaceAll("\\n", " ")
                .replaceAll("\\s+", " ")
                .replaceAll(" ,", ",")
                .trim()
                .replaceAll("^[,\\s]+", "")      // remove leading commas/spaces
                .replaceAll("[,\\s]+$", "") + "...";
    }

}
