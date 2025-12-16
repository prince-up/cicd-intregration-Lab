package com.cicd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TEST RESULT MODEL
 * 
 * This entity stores individual test case results.
 * Each test result is linked to a pipeline execution.
 * 
 * Database Table: test_results
 */
@Entity
@Table(name = "test_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {

    /**
     * Primary Key - Auto-generated ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Foreign Key - Links to PipelineExecution
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id", nullable = false)
    private PipelineExecution execution;

    /**
     * Test Class Name (e.g., UserServiceTest)
     */
    @Column(name = "test_class")
    private String testClass;

    /**
     * Test Method Name (e.g., testUserRegistration)
     */
    @Column(name = "test_method", nullable = false)
    private String testMethod;

    /**
     * Test Status: PASSED, FAILED, SKIPPED, ERROR
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * Test execution time in milliseconds
     */
    @Column(name = "duration_ms")
    private Long durationMs;

    /**
     * Error message if test failed
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Stack trace if test failed
     */
    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    /**
     * Timestamp when test was executed
     */
    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    @PrePersist
    protected void onCreate() {
        if (executedAt == null) {
            executedAt = LocalDateTime.now();
        }
    }
}
