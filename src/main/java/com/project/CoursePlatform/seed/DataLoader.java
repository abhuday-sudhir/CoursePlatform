package com.project.CoursePlatform.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.entity.Topic;
import com.project.CoursePlatform.entity.Subtopic;
import com.project.CoursePlatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {

        if (courseRepository.count() > 0) return;

        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputStream = new ClassPathResource("seed-data.json").getInputStream();

        List<Course> courses = objectMapper.readValue(
                inputStream,
                new TypeReference<List<Course>>() {}
        );

        for (Course course : courses) {
            for (Topic topic : course.getTopics()) {
                topic.setCourse(course);
                for (Subtopic subtopic : topic.getSubtopics()) {
                    subtopic.setTopic(topic);
                }
            }
        }

        courseRepository.saveAll(courses);
    }

}
