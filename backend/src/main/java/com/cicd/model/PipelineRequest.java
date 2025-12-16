package com.cicd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PIPELINE REQUEST DTO (Data Transfer Object)
 * 
 * This class represents the request payload when students
 * trigger a new CI/CD pipeline.
 * 
 * Used for API communication - not stored in database
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineRequest {

    /**
     * Student name who is triggering the pipeline
     */
    private String studentName;

    /**
     * GitHub repository URL
     * Example: https://github.com/username/repo-name
     */
    private String repositoryUrl;

    /**
     * Git branch to build
     * Default: "main" or "master"
     */
    private String branchName;

    /**
     * Optional: Specific commit hash to build
     */
    private String commitHash;
}
