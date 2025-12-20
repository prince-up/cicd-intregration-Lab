package com.cicd.controller;

import com.cicd.model.PipelineRequest;
import com.cicd.model.PipelineResponse;
import com.cicd.model.TestResultResponse;
import com.cicd.service.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PIPELINE CONTROLLER
 * 
 * This REST controller exposes APIs for CI/CD pipeline operations.
 * Frontend applications call these endpoints to trigger builds and fetch status.
 * 
 * BASE URL: /api/pipeline
 * 
 * LEARNING NOTE:
 * @RestController = @Controller + @ResponseBody
 * It automatically serializes return values to JSON
 */
@RestController
@RequestMapping("/api/pipeline")
// @CrossOrigin handled globally in WebConfig
@RequiredArgsConstructor
@Slf4j
public class PipelineController {

    private final PipelineService pipelineService;

    /**
     * ENDPOINT: Trigger a new CI/CD pipeline
     * 
     * POST /api/pipeline/trigger
     * 
     * Request Body:
     * {
     *   "studentName": "John Doe",
     *   "repositoryUrl": "https://github.com/user/repo",
     *   "branchName": "main"
     * }
     * 
     * Response: PipelineResponse with execution details
     */
    @PostMapping("/trigger")
    public ResponseEntity<PipelineResponse> triggerPipeline(@RequestBody PipelineRequest request) {
        log.info("API: Trigger pipeline request received from student: {}", request.getStudentName());
        
        try {
            // Validate request
            if (request.getStudentName() == null || request.getStudentName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getRepositoryUrl() == null || request.getRepositoryUrl().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Trigger pipeline
            PipelineResponse response = pipelineService.triggerPipeline(request);
            
            log.info("Pipeline triggered successfully. Execution ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Error triggering pipeline", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT: Get all pipeline executions
     * 
     * GET /api/pipeline/executions
     * 
     * Response: List of all pipeline executions (newest first)
     */
    @GetMapping("/executions")
    public ResponseEntity<List<PipelineResponse>> getAllExecutions() {
        log.info("API: Fetching all pipeline executions");
        
        try {
            List<PipelineResponse> executions = pipelineService.getAllExecutions();
            return ResponseEntity.ok(executions);
        } catch (Exception e) {
            log.error("Error fetching executions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT: Get pipeline execution by ID
     * 
     * GET /api/pipeline/executions/{id}
     * 
     * Response: Pipeline execution details
     */
    @GetMapping("/executions/{id}")
    public ResponseEntity<PipelineResponse> getExecutionById(@PathVariable Long id) {
        log.info("API: Fetching pipeline execution with ID: {}", id);
        
        try {
            PipelineResponse execution = pipelineService.getExecutionById(id);
            return ResponseEntity.ok(execution);
        } catch (RuntimeException e) {
            log.error("Execution not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching execution", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT: Get executions by student name
     * 
     * GET /api/pipeline/student/{studentName}
     * 
     * Response: List of student's pipeline executions
     */
    @GetMapping("/student/{studentName}")
    public ResponseEntity<List<PipelineResponse>> getStudentExecutions(@PathVariable String studentName) {
        log.info("API: Fetching executions for student: {}", studentName);
        
        try {
            List<PipelineResponse> executions = pipelineService.getExecutionsByStudent(studentName);
            return ResponseEntity.ok(executions);
        } catch (Exception e) {
            log.error("Error fetching student executions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT: Get test results for a specific execution
     * 
     * GET /api/pipeline/executions/{id}/tests
     * 
     * Response: List of test results with pass/fail status
     */
    @GetMapping("/executions/{id}/tests")
    public ResponseEntity<List<TestResultResponse>> getTestResults(@PathVariable Long id) {
        log.info("API: Fetching test results for execution: {}", id);
        
        try {
            List<TestResultResponse> testResults = pipelineService.getTestResults(id);
            return ResponseEntity.ok(testResults);
        } catch (Exception e) {
            log.error("Error fetching test results", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT: Update pipeline status (called by Jenkins webhook)
     * 
     * PUT /api/pipeline/executions/{id}/status
     * 
     * Request Body:
     * {
     *   "status": "SUCCESS",
     *   "stage": "DEPLOY"
     * }
     */
    @PutMapping("/executions/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        
        log.info("API: Updating status for execution: {}", id);
        
        try {
            String status = statusUpdate.get("status");
            String stage = statusUpdate.get("stage");
            
            pipelineService.updateExecutionStatus(id, status, stage);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT: Health check
     * 
     * GET /api/pipeline/health
     * 
     * Response: System status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Student CI/CD Lab");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }
    
    @Autowired
    private com.cicd.service.GitHubService gitHubService;

    /**
     * ENDPOINT: Get recent commits
     * GET /api/pipeline/github/commits
     */
    @GetMapping("/github/commits")
    public ResponseEntity<String> getRecentCommits(@RequestParam String repoUrl) {
        log.info("API: Fetching recent commits for: {}", repoUrl);
        String commits = gitHubService.getRecentCommits(repoUrl);
        return ResponseEntity.ok(commits);
    }
}
