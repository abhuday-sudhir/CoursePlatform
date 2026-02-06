package com.project.CoursePlatform.repository;

import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.entity.Subtopic;
import com.project.CoursePlatform.entity.SubtopicProgress;
import com.project.CoursePlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubtopicProgressRepository extends JpaRepository<SubtopicProgress, Long> {
    boolean existsByUserAndSubtopic(User user, Subtopic subtopic);
    Optional<SubtopicProgress> findByUserAndSubtopic(User user, Subtopic subtopic);
    List<SubtopicProgress> findByUserAndSubtopic_Topic_Course(
            User user,
            Course course
    );
}
