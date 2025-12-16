package com.cicd.repository;

import com.cicd.model.PipelineExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PIPELINE EXECUTION REPOSITORY
 * 
 * This interface provides database operations for PipelineExecution entity.
 * Spring Data JPA automatically implements these methods.
 * 
 * No need to write SQL queries - JPA does it for you!
 */
@Repository
public interface PipelineExecutionRepository extends JpaRepository<PipelineExecution, Long> {

    /**
     * Find all executions by student name, ordered by creation date (newest first)
     * 
     * @param studentName Student name to search for
     * @return List of pipeline executions
     */
    List<PipelineExecution> findByStudentNameOrderByCreatedAtDesc(String studentName);

    /**
     * Find execution by build number
     * 
     * @param buildNumber Jenkins build number
     * @return PipelineExecution if found
     */
    PipelineExecution findByBuildNumber(Integer buildNumber);

    /**
     * Find all executions, ordered by creation date (newest first)
     * 
     * @return List of all pipeline executions
     */
    List<PipelineExecution> findAllByOrderByCreatedAtDesc();

    /**
     * Find executions by status
     * 
     * @param status Pipeline status (SUCCESS, FAILED, RUNNING, etc.)
     * @return List of matching executions
     */
    List<PipelineExecution> findByStatusOrderByCreatedAtDesc(String status);
}
