package com.cicd.service;

import com.cicd.model.PipelineExecution;
import com.cicd.repository.PipelineExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PIPELINE STATUS UPDATER
 * 
 * Automatically polls Jenkins every 5 seconds to update build status for running pipelines.
 * This ensures the dashboard shows real-time progress without manual refresh.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PipelineStatusUpdater {

    private final PipelineExecutionRepository pipelineRepository;
    private final JenkinsService jenkinsService;
    private final GitHubService gitHubService;
    private final NotificationService notificationService;

    /**
     * Scheduled task that runs every 5 seconds
     * Updates status for all RUNNING pipelines by checking Jenkins
     */
    @Scheduled(fixedDelay = 5000) // Run every 5 seconds
    public void updateRunningPipelines() {
        try {
            // Find all executions that are still running
            List<PipelineExecution> runningExecutions = pipelineRepository
                .findAll()
                .stream()
                .filter(exec -> "RUNNING".equals(exec.getStatus()))
                .toList();

            if (!runningExecutions.isEmpty()) {
                log.debug("Found {} running pipelines to update", runningExecutions.size());
            }

            // Update each running execution
            for (PipelineExecution execution : runningExecutions) {
                try {
                    log.debug("Checking Jenkins status for execution ID {} (Build #{})", 
                             execution.getId(), execution.getBuildNumber());
                    
                    // Update GitHub status to pending if not already done
                    if (execution.getCommitHash() != null && "RUNNING".equals(execution.getStatus())) {
                        gitHubService.updateCommitStatus(
                            execution.getRepositoryUrl(),
                            execution.getCommitHash(),
                            "pending",
                            "Build #" + execution.getBuildNumber() + " in progress",
                            "Jenkins CI/CD"
                        );
                    }
                    
                    // Fetch latest status from Jenkins
                    String jenkinsStatus = jenkinsService.getBuildStatus(execution.getBuildNumber());
                    
                    if (jenkinsStatus != null && !"RUNNING".equals(jenkinsStatus)) {
                        // Build completed - update final status
                        execution.setStatus(jenkinsStatus);
                        if ("SUCCESS".equals(jenkinsStatus)) {
                            execution.setCurrentStage("Deploy");
                        } else if ("FAILURE".equals(jenkinsStatus)) {
                            execution.setCurrentStage("Failed");
                        }
                        
                        // Update GitHub commit status
                        if (execution.getCommitHash() != null) {
                            String githubState = "SUCCESS".equals(jenkinsStatus) ? "success" : "failure";
                            String description = "SUCCESS".equals(jenkinsStatus) 
                                ? "All checks passed ✓" 
                                : "Build failed ✗";
                            
                            gitHubService.updateCommitStatus(
                                execution.getRepositoryUrl(),
                                execution.getCommitHash(),
                                githubState,
                                description,
                                "Jenkins CI/CD"
                            );
                        }
                        execution.setCompletedAt(java.time.LocalDateTime.now());
                        pipelineRepository.save(execution);

                        // Notify completion
                        notificationService.sendPipelineCompletion(
                            execution.getStudentName(), 
                            jenkinsStatus, 
                            (long) execution.getBuildNumber()
                        );
                        
                        log.info("Updated execution {} to status: {}", 
                                execution.getId(), jenkinsStatus);
                    }
                } catch (Exception e) {
                    log.error("Failed to update execution {}: {}", 
                             execution.getId(), e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in scheduled pipeline update: {}", e.getMessage());
        }
    }
}
