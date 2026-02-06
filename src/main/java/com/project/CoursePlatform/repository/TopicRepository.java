package com.project.CoursePlatform.repository;

import com.project.CoursePlatform.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,String> {
}
