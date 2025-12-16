package com.cicd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PIPELINE RESPONSE DTO (Data Transfer Object)
 * 
 * This class represents the response sent to the frontend
 * containing pipeline execution details.
 * 
 * Used for API communication - not stored in database
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineResponse {

    /**
     * Execution ID
     */
    private Long id;

    /**
     * Build number from Jenkins
     */
    private Integer buildNumber;

    /**
     * Student name
     */
    private String studentName;

    /**
     * Repository URL
     */
    private String repositoryUrl;

    /**
     * Branch name
     */
    private String branchName;

    /**
     * Overall pipeline status
     */
    private String status;

    /**
     * Current stage being executed
     */
    private String currentStage;

    /**
     * Build status
     */
    private String buildStatus;

    /**
     * Test status
     */
    private String testStatus;

    /**
     * Test statistics
     */
    private Integer totalTests;
    private Integer testsPassed;
    private Integer testsFailed;

    /**
     * Deployment status
     */
    private String deploymentStatus;

    /**
     * Duration in seconds
     */
    private Long duration;

    /**
     * Error message if any
     */
    private String errorMessage;

    /**
     * Timestamps
     */
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    /**
     * Test results details
     */
    private List<TestResultResponse> testResults;
}
