package com.cicd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * GITHUB SERVICE
 * 
 * Updates commit status on GitHub repository
 * Shows green/red checkmark on commits
 */
@Service
@Slf4j
public class GitHubService {

    @Value("${github.token}")
    private String githubToken;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Update commit status on GitHub
     * 
     * @param repoUrl GitHub repository URL (e.g., https://github.com/prince-up/cicd-integration-Lab.git)
     * @param commitSha Commit hash
     * @param state Status: pending, success, failure, error
     * @param description Status description
     * @param buildUrl Jenkins build URL
     */
    public void updateCommitStatus(String repoUrl, String commitSha, String state, String description, String buildUrl) {
        try {
            // Extract owner and repo from URL
            // https://github.com/prince-up/cicd-integration-Lab.git -> prince-up/cicd-integration-Lab
            String[] parts = repoUrl.replace("https://github.com/", "")
                                   .replace(".git", "")
                                   .split("/");
            
            if (parts.length < 2) {
                log.warn("Invalid GitHub URL format: {}", repoUrl);
                return;
            }
            
            String owner = parts[0];
            String repo = parts[1];
            
            // GitHub API endpoint
            String apiUrl = String.format("https://api.github.com/repos/%s/%s/statuses/%s", 
                                         owner, repo, commitSha);
            
            // Build JSON payload
            String json = String.format(
                "{\"state\":\"%s\",\"target_url\":\"%s\",\"description\":\"%s\",\"context\":\"Jenkins CI/CD\"}",
                state, buildUrl, description
            );
            
            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + githubToken)
                .header("Accept", "application/vnd.github+json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            
            // Send request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            
            if (response.statusCode() == 201) {
                log.info("✓ GitHub commit status updated: {} - {}", state, description);
            } else {
                log.warn("GitHub API returned status {}: {}", response.statusCode(), response.body());
            }
            
        } catch (Exception e) {
            log.error("Failed to update GitHub commit status: {}", e.getMessage());
        }
    }

    /**
     * Get recent commits for a repository
     */
    public String getRecentCommits(String repoUrl) {
        try {
            String[] parts = repoUrl.replace("https://github.com/", "")
                                   .replace(".git", "")
                                   .split("/");
            
            if (parts.length < 2) return "[]";
            
            String owner = parts[0];
            String repo = parts[1];
            
            // Fetch last 5 commits
            String apiUrl = String.format("https://api.github.com/repos/%s/%s/commits?per_page=5", owner, repo);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + githubToken)
                .header("Accept", "application/vnd.github+json")
                .GET()
                .build();
                
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                log.warn("Failed to fetch commits. Status: {}", response.statusCode());
                return "[]";
            }
            
        } catch (Exception e) {
            log.error("Error fetching commits: {}", e.getMessage());
            return "[]";
        }
    }
}
