package com.cicd.repository;

import com.cicd.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TEST RESULT REPOSITORY
 * 
 * This interface provides database operations for TestResult entity.
 */
@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    /**
     * Find all test results for a specific pipeline execution
     * 
     * @param executionId Pipeline execution ID
     * @return List of test results
     */
    List<TestResult> findByExecutionId(Long executionId);

    /**
     * Find failed test results for a specific execution
     * 
     * @param executionId Pipeline execution ID
     * @param status Test status (e.g., "FAILED")
     * @return List of failed test results
     */
    List<TestResult> findByExecutionIdAndStatus(Long executionId, String status);
}
