package com.cicd.controller;

import com.cicd.model.PipelineRequest;
import com.cicd.model.PipelineResponse;
import com.cicd.service.PipelineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PIPELINE CONTROLLER TEST
 * 
 * This class tests the REST API endpoints of PipelineController.
 * 
 * LEARNING NOTE:
 * - @SpringBootTest starts the entire Spring application
 * - TestRestTemplate is used to make HTTP requests
 * - This is an Integration Test (tests full stack)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Pipeline Controller Integration Tests")
public class PipelineControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PipelineService pipelineService;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/pipeline";
    }

    /**
     * Test: Health check endpoint
     * 
     * Verifies that the API is running and responding
     */
    @Test
    @DisplayName("Health check should return UP status")
    void testHealthCheck() {
        // Act: Call health endpoint
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/health", 
            String.class
        );

        // Assert: Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("UP"));
    }

    /**
     * Test: Trigger pipeline with valid request
     * 
     * Verifies that pipeline can be triggered successfully
     */
    @Test
    @DisplayName("Trigger pipeline should create new execution")
    void testTriggerPipeline() {
        // Arrange: Create pipeline request
        PipelineRequest request = PipelineRequest.builder()
            .studentName("Test Student")
            .repositoryUrl("https://github.com/test/repo")
            .branchName("main")
            .build();

        // Act: Trigger pipeline
        ResponseEntity<PipelineResponse> response = restTemplate.postForEntity(
            baseUrl + "/trigger",
            request,
            PipelineResponse.class
        );

        // Assert: Verify response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Student", response.getBody().getStudentName());
        assertNotNull(response.getBody().getId());
    }

    /**
     * Test: Get all executions
     * 
     * Verifies that the API returns list of executions
     */
    @Test
    @DisplayName("Get all executions should return list")
    void testGetAllExecutions() {
        // Arrange: Trigger a pipeline first
        PipelineRequest request = PipelineRequest.builder()
            .studentName("Test Student 2")
            .repositoryUrl("https://github.com/test/repo2")
            .branchName("main")
            .build();
        pipelineService.triggerPipeline(request);

        // Act: Get all executions
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/executions",
            String.class
        );

        // Assert: Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Test Student"));
    }

    /**
     * Test: Get execution by ID
     * 
     * Verifies that specific execution can be retrieved
     */
    @Test
    @DisplayName("Get execution by ID should return execution details")
    void testGetExecutionById() {
        // Arrange: Create an execution
        PipelineRequest request = PipelineRequest.builder()
            .studentName("Test Student 3")
            .repositoryUrl("https://github.com/test/repo3")
            .branchName("develop")
            .build();
        PipelineResponse created = pipelineService.triggerPipeline(request);

        // Act: Get execution by ID
        ResponseEntity<PipelineResponse> response = restTemplate.getForEntity(
            baseUrl + "/executions/" + created.getId(),
            PipelineResponse.class
        );

        // Assert: Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(created.getId(), response.getBody().getId());
        assertEquals("Test Student 3", response.getBody().getStudentName());
    }

    /**
     * Test: Invalid pipeline request (missing student name)
     * 
     * Verifies that validation works correctly
     */
    @Test
    @DisplayName("Trigger pipeline with missing student name should fail")
    void testTriggerPipelineInvalidRequest() {
        // Arrange: Create invalid request (no student name)
        PipelineRequest request = PipelineRequest.builder()
            .repositoryUrl("https://github.com/test/repo")
            .branchName("main")
            .build();

        // Act: Try to trigger pipeline
        ResponseEntity<PipelineResponse> response = restTemplate.postForEntity(
            baseUrl + "/trigger",
            request,
            PipelineResponse.class
        );

        // Assert: Should return bad request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
