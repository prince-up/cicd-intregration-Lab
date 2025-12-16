package com.cicd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * PIPELINE EXECUTION MODEL
 * 
 * This entity represents a single CI/CD pipeline execution.
 * It stores information about each build, test, and deployment.
 * 
 * Database Table: pipeline_executions
 */
@Entity
@Table(name = "pipeline_executions")
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates no-args constructor
@AllArgsConstructor // Lombok: Generates all-args constructor
public class PipelineExecution {

    /**
     * Primary Key - Auto-generated ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Jenkins Build Number
     * Each execution in Jenkins gets a unique build number
     */
    @Column(name = "build_number")
    private Integer buildNumber;

    /**
     * Student/Developer name who triggered the pipeline
     */
    @Column(name = "student_name", nullable = false)
    private String studentName;

    /**
     * GitHub Repository URL
     */
    @Column(name = "repository_url")
    private String repositoryUrl;

    /**
     * Git Branch Name (e.g., main, develop, feature/login)
     */
    @Column(name = "branch_name")
    private String branchName;

    /**
     * Git Commit Hash (SHA)
     */
    @Column(name = "commit_hash")
    private String commitHash;

    /**
     * Pipeline Status: PENDING, RUNNING, SUCCESS, FAILED, ABORTED
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * Current Stage: CHECKOUT, BUILD, TEST, DEPLOY
     */
    @Column(name = "current_stage")
    private String currentStage;

    /**
     * Build Status: SUCCESS, FAILED, SKIPPED
     */
    @Column(name = "build_status")
    private String buildStatus;

    /**
     * Test Status: PASSED, FAILED, SKIPPED
     */
    @Column(name = "test_status")
    private String testStatus;

    /**
     * Total number of tests executed
     */
    @Column(name = "total_tests")
    private Integer totalTests;

    /**
     * Number of tests passed
     */
    @Column(name = "tests_passed")
    private Integer testsPassed;

    /**
     * Number of tests failed
     */
    @Column(name = "tests_failed")
    private Integer testsFailed;

    /**
     * Deployment Status: SUCCESS, FAILED, SKIPPED
     */
    @Column(name = "deployment_status")
    private String deploymentStatus;

    /**
     * Build duration in seconds
     */
    @Column(name = "duration")
    private Long duration;

    /**
     * Console output logs from Jenkins
     */
    @Column(name = "console_output", columnDefinition = "TEXT")
    private String consoleOutput;

    /**
     * Error message if pipeline failed
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Timestamp when pipeline started
     */
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    /**
     * Timestamp when pipeline completed
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Timestamp when record was created
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when record was last updated
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Auto-set timestamps before persisting
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }

    /**
     * Update timestamp before updating
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
