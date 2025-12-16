package com.cicd.service;

import com.cicd.model.*;
import com.cicd.repository.PipelineExecutionRepository;
import com.cicd.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PIPELINE SERVICE
 * 
 * This service handles all business logic related to CI/CD pipeline operations.
 * It orchestrates Jenkins integration, database operations, and data transformation.
 * 
 * @Slf4j - Lombok annotation that provides logger
 * @RequiredArgsConstructor - Lombok generates constructor for final fields
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PipelineService {

    private final PipelineExecutionRepository pipelineRepository;
    private final TestResultRepository testResultRepository;
    private final JenkinsService jenkinsService;

    /**
     * Trigger a new CI/CD pipeline
     * 
     * This method:
     * 1. Creates a new pipeline execution record
     * 2. Triggers Jenkins build
     * 3. Returns execution details
     * 
     * @param request Pipeline request with repo details
     * @return PipelineResponse with execution details
     */
    @Transactional
    public PipelineResponse triggerPipeline(PipelineRequest request) {
        log.info("Triggering pipeline for student: {}, repo: {}", 
                 request.getStudentName(), request.getRepositoryUrl());

        // Create new pipeline execution record
        PipelineExecution execution = new PipelineExecution();
        execution.setStudentName(request.getStudentName());
        execution.setRepositoryUrl(request.getRepositoryUrl());
        execution.setBranchName(request.getBranchName() != null ? request.getBranchName() : "main");
        execution.setCommitHash(request.getCommitHash());
        execution.setStatus("PENDING");
        execution.setCurrentStage("INITIALIZED");
        execution.setStartedAt(LocalDateTime.now());

        // Save to database
        execution = pipelineRepository.save(execution);
        log.info("Created pipeline execution with ID: {}", execution.getId());

        // Trigger Jenkins build (async)
        try {
            Integer buildNumber = jenkinsService.triggerBuild(request);
            execution.setBuildNumber(buildNumber);
            execution.setStatus("RUNNING");
            execution.setCurrentStage("CHECKOUT");
            pipelineRepository.save(execution);
            log.info("Jenkins build triggered successfully. Build number: {}", buildNumber);
        } catch (Exception e) {
            log.error("Failed to trigger Jenkins build", e);
            execution.setStatus("FAILED");
            execution.setErrorMessage("Failed to trigger Jenkins: " + e.getMessage());
            pipelineRepository.save(execution);
        }

        return convertToResponse(execution);
    }

    /**
     * Get all pipeline executions
     * 
     * @return List of all pipeline executions
     */
    public List<PipelineResponse> getAllExecutions() {
        log.info("Fetching all pipeline executions");
        List<PipelineExecution> executions = pipelineRepository.findAllByOrderByCreatedAtDesc();
        return executions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get pipeline execution by ID
     * 
     * @param id Execution ID
     * @return Pipeline execution details
     */
    public PipelineResponse getExecutionById(Long id) {
        log.info("Fetching pipeline execution with ID: {}", id);
        PipelineExecution execution = pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline execution not found with ID: " + id));
        
        return convertToResponse(execution);
    }

    /**
     * Get executions by student name
     * 
     * @param studentName Student name
     * @return List of student's pipeline executions
     */
    public List<PipelineResponse> getExecutionsByStudent(String studentName) {
        log.info("Fetching executions for student: {}", studentName);
        List<PipelineExecution> executions = pipelineRepository.findByStudentNameOrderByCreatedAtDesc(studentName);
        return executions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update pipeline execution status
     * 
     * This method is called by Jenkins webhook or polling mechanism
     * to update the pipeline status
     * 
     * @param id Execution ID
     * @param status New status
     * @param stage Current stage
     */
    @Transactional
    public void updateExecutionStatus(Long id, String status, String stage) {
        log.info("Updating execution {} - Status: {}, Stage: {}", id, status, stage);
        
        PipelineExecution execution = pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline execution not found"));
        
        execution.setStatus(status);
        execution.setCurrentStage(stage);
        
        if ("SUCCESS".equals(status) || "FAILED".equals(status) || "ABORTED".equals(status)) {
            execution.setCompletedAt(LocalDateTime.now());
            
            // Calculate duration
            if (execution.getStartedAt() != null) {
                long duration = java.time.Duration.between(
                    execution.getStartedAt(), 
                    execution.getCompletedAt()
                ).getSeconds();
                execution.setDuration(duration);
            }
        }
        
        pipelineRepository.save(execution);
    }

    /**
     * Update build status
     * 
     * @param id Execution ID
     * @param buildStatus Build status (SUCCESS, FAILED)
     */
    @Transactional
    public void updateBuildStatus(Long id, String buildStatus) {
        log.info("Updating build status for execution {}: {}", id, buildStatus);
        PipelineExecution execution = pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline execution not found"));
        execution.setBuildStatus(buildStatus);
        pipelineRepository.save(execution);
    }

    /**
     * Update test results
     * 
     * @param id Execution ID
     * @param testStatus Overall test status
     * @param total Total tests
     * @param passed Passed tests
     * @param failed Failed tests
     */
    @Transactional
    public void updateTestResults(Long id, String testStatus, int total, int passed, int failed) {
        log.info("Updating test results for execution {}: Total={}, Passed={}, Failed={}", 
                 id, total, passed, failed);
        
        PipelineExecution execution = pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline execution not found"));
        
        execution.setTestStatus(testStatus);
        execution.setTotalTests(total);
        execution.setTestsPassed(passed);
        execution.setTestsFailed(failed);
        
        pipelineRepository.save(execution);
    }

    /**
     * Update deployment status
     * 
     * @param id Execution ID
     * @param deploymentStatus Deployment status
     */
    @Transactional
    public void updateDeploymentStatus(Long id, String deploymentStatus) {
        log.info("Updating deployment status for execution {}: {}", id, deploymentStatus);
        PipelineExecution execution = pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline execution not found"));
        execution.setDeploymentStatus(deploymentStatus);
        pipelineRepository.save(execution);
    }

    /**
     * Get test results for a specific execution
     * 
     * @param executionId Execution ID
     * @return List of test results
     */
    public List<TestResultResponse> getTestResults(Long executionId) {
        log.info("Fetching test results for execution: {}", executionId);
        List<TestResult> results = testResultRepository.findByExecutionId(executionId);
        
        return results.stream()
                .map(this::convertTestResultToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert PipelineExecution entity to PipelineResponse DTO
     */
    private PipelineResponse convertToResponse(PipelineExecution execution) {
        return PipelineResponse.builder()
                .id(execution.getId())
                .buildNumber(execution.getBuildNumber())
                .studentName(execution.getStudentName())
                .repositoryUrl(execution.getRepositoryUrl())
                .branchName(execution.getBranchName())
                .status(execution.getStatus())
                .currentStage(execution.getCurrentStage())
                .buildStatus(execution.getBuildStatus())
                .testStatus(execution.getTestStatus())
                .totalTests(execution.getTotalTests())
                .testsPassed(execution.getTestsPassed())
                .testsFailed(execution.getTestsFailed())
                .deploymentStatus(execution.getDeploymentStatus())
                .duration(execution.getDuration())
                .errorMessage(execution.getErrorMessage())
                .startedAt(execution.getStartedAt())
                .completedAt(execution.getCompletedAt())
                .build();
    }

    /**
     * Convert TestResult entity to TestResultResponse DTO
     */
    private TestResultResponse convertTestResultToResponse(TestResult result) {
        return TestResultResponse.builder()
                .id(result.getId())
                .testClass(result.getTestClass())
                .testMethod(result.getTestMethod())
                .status(result.getStatus())
                .durationMs(result.getDurationMs())
                .errorMessage(result.getErrorMessage())
                .build();
    }
}
