package com.project.CoursePlatform.repository;

import com.project.CoursePlatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchRepository extends JpaRepository<Course, String> {

    @Query(value = """
        SELECT DISTINCT
            c.id as course_id,
            c.title as course_title,
            t.title as topic_title,
            s.id as subtopic_id,
            s.title as subtopic_title,

            CASE
              WHEN similarity(s.title, :query) > 0.4 THEN 'subtopic'
              ELSE 'content'
            END as match_type,

            (
                ts_rank_cd(s.search_vector, to_tsquery('english', :query || ':*'))
                + similarity(s.title, :query)
            ) as rank,

            regexp_replace(
                ts_headline(
                    'english',
                    s.content,
                    to_tsquery('english', :query || ':*'),
                    'StartSel=, StopSel=, MaxWords=18, MinWords=8'
                ),
                E'<[^>]+>', '', 'g'
            ) as snippet

        FROM course c
        JOIN topic t ON t.course_id = c.id
        JOIN subtopic s ON s.topic_id = t.id

        WHERE
              s.search_vector @@ to_tsquery('english', :query || ':*')  -- PREFIX MATCH
           OR similarity(s.title, :query) > 0.4                         -- TYPO MATCH
           OR LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%'))       -- PARTIAL MATCH

        ORDER BY rank DESC
        """, nativeQuery = true)
    List<Object[]> search(@Param("query") String query);
}
