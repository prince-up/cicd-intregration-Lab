package com.cicd.service;

import com.cicd.model.PipelineExecution;
import com.cicd.model.PipelineRequest;
import com.cicd.model.PipelineResponse;
import com.cicd.repository.PipelineExecutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PIPELINE SERVICE TEST
 * 
 * This class tests the business logic in PipelineService.
 * 
 * LEARNING NOTE:
 * These are integration tests that test the service layer
 * with real database operations (using H2 in-memory database)
 */
@SpringBootTest
@DisplayName("Pipeline Service Tests")
public class PipelineServiceTest {

    @Autowired
    private PipelineService pipelineService;

    @Autowired
    private PipelineExecutionRepository pipelineRepository;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        pipelineRepository.deleteAll();
    }

    /**
     * Test: Trigger pipeline creates new execution
     */
    @Test
    @DisplayName("Trigger pipeline should create and save execution")
    void testTriggerPipeline() {
        // Arrange
        PipelineRequest request = PipelineRequest.builder()
            .studentName("Alice Johnson")
            .repositoryUrl("https://github.com/alice/my-project")
            .branchName("feature/new-feature")
            .build();

        // Act
        PipelineResponse response = pipelineService.triggerPipeline(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Alice Johnson", response.getStudentName());
        assertEquals("https://github.com/alice/my-project", response.getRepositoryUrl());
        assertEquals("feature/new-feature", response.getBranchName());
        assertNotNull(response.getBuildNumber());
        assertTrue(response.getStatus().equals("PENDING") || response.getStatus().equals("RUNNING"));
    }

    /**
     * Test: Get all executions
     */
    @Test
    @DisplayName("Get all executions should return all saved executions")
    void testGetAllExecutions() {
        // Arrange: Create multiple executions
        createExecution("Bob Smith", "https://github.com/bob/repo1");
        createExecution("Charlie Brown", "https://github.com/charlie/repo2");
        createExecution("Diana Prince", "https://github.com/diana/repo3");

        // Act
        List<PipelineResponse> executions = pipelineService.getAllExecutions();

        // Assert
        assertEquals(3, executions.size());
    }

    /**
     * Test: Get executions by student name
     */
    @Test
    @DisplayName("Get executions by student should return only that student's executions")
    void testGetExecutionsByStudent() {
        // Arrange: Create executions for different students
        createExecution("Student A", "https://github.com/a/repo1");
        createExecution("Student A", "https://github.com/a/repo2");
        createExecution("Student B", "https://github.com/b/repo1");

        // Act
        List<PipelineResponse> studentAExecutions = pipelineService.getExecutionsByStudent("Student A");

        // Assert
        assertEquals(2, studentAExecutions.size());
        studentAExecutions.forEach(exec -> 
            assertEquals("Student A", exec.getStudentName())
        );
    }

    /**
     * Test: Update execution status
     */
    @Test
    @DisplayName("Update status should modify execution correctly")
    void testUpdateExecutionStatus() {
        // Arrange: Create execution
        PipelineRequest request = PipelineRequest.builder()
            .studentName("Test User")
            .repositoryUrl("https://github.com/test/repo")
            .branchName("main")
            .build();
        PipelineResponse created = pipelineService.triggerPipeline(request);

        // Act: Update status
        pipelineService.updateExecutionStatus(created.getId(), "SUCCESS", "COMPLETED");

        // Assert: Verify update
        PipelineResponse updated = pipelineService.getExecutionById(created.getId());
        assertEquals("SUCCESS", updated.getStatus());
        assertEquals("COMPLETED", updated.getCurrentStage());
        assertNotNull(updated.getCompletedAt());
    }

    /**
     * Test: Update build status
     */
    @Test
    @DisplayName("Update build status should modify build status field")
    void testUpdateBuildStatus() {
        // Arrange
        PipelineRequest request = PipelineRequest.builder()
            .studentName("Build Test User")
            .repositoryUrl("https://github.com/build/test")
            .branchName("main")
            .build();
        PipelineResponse created = pipelineService.triggerPipeline(request);

        // Act
        pipelineService.updateBuildStatus(created.getId(), "SUCCESS");

        // Assert
        PipelineResponse updated = pipelineService.getExecutionById(created.getId());
        assertEquals("SUCCESS", updated.getBuildStatus());
    }

    /**
     * Test: Update test results
     */
    @Test
    @DisplayName("Update test results should save test statistics")
    void testUpdateTestResults() {
        // Arrange
        PipelineRequest request = PipelineRequest.builder()
            .studentName("Test Results User")
            .repositoryUrl("https://github.com/test/results")
            .branchName("main")
            .build();
        PipelineResponse created = pipelineService.triggerPipeline(request);

        // Act
        pipelineService.updateTestResults(created.getId(), "PASSED", 10, 9, 1);

        // Assert
        PipelineResponse updated = pipelineService.getExecutionById(created.getId());
        assertEquals("PASSED", updated.getTestStatus());
        assertEquals(10, updated.getTotalTests());
        assertEquals(9, updated.getTestsPassed());
        assertEquals(1, updated.getTestsFailed());
    }

    /**
     * Test: Get execution by ID throws exception for invalid ID
     */
    @Test
    @DisplayName("Get execution with invalid ID should throw exception")
    void testGetExecutionByIdNotFound() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pipelineService.getExecutionById(99999L);
        });
    }

    /**
     * Helper method to create a pipeline execution
     */
    private void createExecution(String studentName, String repoUrl) {
        PipelineRequest request = PipelineRequest.builder()
            .studentName(studentName)
            .repositoryUrl(repoUrl)
            .branchName("main")
            .build();
        pipelineService.triggerPipeline(request);
    }
}
