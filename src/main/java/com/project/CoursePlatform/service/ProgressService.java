package com.project.CoursePlatform.service;

import com.project.CoursePlatform.Dto.ProgressCompleteDto;
import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.entity.Subtopic;
import com.project.CoursePlatform.entity.SubtopicProgress;
import com.project.CoursePlatform.entity.User;
import com.project.CoursePlatform.exception.ForbiddenException;
import com.project.CoursePlatform.exception.ResourceNotFoundException;
import com.project.CoursePlatform.repository.EnrollmentRepository;
import com.project.CoursePlatform.repository.SubtopicProgressRepository;
import com.project.CoursePlatform.repository.SubtopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ProgressService {

    @Autowired
    SubtopicProgressRepository subtopicProgressRepository;

    @Autowired
    SubtopicRepository subtopicRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    CurrentUserService currentUserService;

    public ProgressCompleteDto completeSubtopic(String subtopicId) {

        User user = currentUserService.getCurrentUser();

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subtopic with id '" + subtopicId + "' does not exist"
                ));

        Course course = subtopic.getTopic().getCourse();

        if (!enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new ForbiddenException(
                    "You must be enrolled in this course to mark subtopics as complete"
            );
        }

        SubtopicProgress progress = subtopicProgressRepository
                .findByUserAndSubtopic(user, subtopic)
                .orElse(null);

        if (progress == null) {
            progress = new SubtopicProgress();
            progress.setUser(user);
            progress.setSubtopic(subtopic);
        }

        progress.setCompleted(true);
        progress.setCompletedAt(Instant.now());

        subtopicProgressRepository.save(progress);

        return new ProgressCompleteDto(
                subtopic.getId(),
                true,
                progress.getCompletedAt()
        );
    }

}
