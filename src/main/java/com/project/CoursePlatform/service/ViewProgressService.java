package com.project.CoursePlatform.service;

import com.project.CoursePlatform.Dto.CompletedItemDto;
import com.project.CoursePlatform.Dto.ProgressResponseDto;
import com.project.CoursePlatform.entity.Course;
import com.project.CoursePlatform.entity.Enrollment;
import com.project.CoursePlatform.entity.SubtopicProgress;
import com.project.CoursePlatform.entity.User;
import com.project.CoursePlatform.repository.EnrollmentRepository;
import com.project.CoursePlatform.repository.SubtopicProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewProgressService {
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    SubtopicProgressRepository subtopicProgressRepository;

    @Autowired
    CurrentUserService currentUserService;

    public ProgressResponseDto viewProgress(Long enrollmentId)
    {
        User user=currentUserService.getCurrentUser();
        Enrollment enrollment=enrollmentRepository.findByIdAndUser(enrollmentId,user)
                .orElseThrow(() -> new RuntimeException("Enrollement Not Found"));

        Course course=enrollment.getCourse();
        int total=course.getTopics().stream().mapToInt(t -> t.getSubtopics().size()).sum();

        List<SubtopicProgress> completed=subtopicProgressRepository.findByUserAndSubtopic_Topic_Course(user,course);
        double percent = total == 0 ? 0 : (completed.size() * 100.0 / total);
        return new ProgressResponseDto(
                enrollmentId,
                course.getId(),
                course.getTitle(),
                total,
                completed.size(),
                percent,
                completed.stream().map(p ->
                        new CompletedItemDto(
                                p.getSubtopic().getId(),
                                p.getSubtopic().getTitle(),
                                p.getCompletedAt()
                        )).toList()
        );
    }
}
