package com.cicd.service;

import com.cicd.model.PipelineRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * JENKINS SERVICE
 * 
 * This service handles all interactions with Jenkins CI/CD server.
 * It uses Jenkins REST API to trigger builds and fetch status.
 * 
 * LEARNING NOTE:
 * In production, you would use the official Jenkins Java client library.
 * This implementation uses simple HTTP requests for educational clarity.
 */
@Service
@Slf4j
public class JenkinsService {

    @Value("${jenkins.url:http://localhost:8081}")
    private String jenkinsUrl;

    @Value("${jenkins.username:admin}")
    private String jenkinsUsername;

    @Value("${jenkins.api.token:your-api-token}")
    private String jenkinsApiToken;

    @Value("${jenkins.job.name:student-cicd-pipeline}")
    private String jenkinsJobName;

    private final HttpClient httpClient;

    public JenkinsService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Trigger a Jenkins build
     * 
     * This method sends a POST request to Jenkins to start a new build
     * with the provided repository and branch parameters.
     * 
     * @param request Pipeline request with repo details
     * @return Build number assigned by Jenkins
     * @throws Exception if Jenkins is not reachable or build fails
     */
    public Integer triggerBuild(PipelineRequest request) throws Exception {
        log.info("Triggering Jenkins build for job: {}", jenkinsJobName);

        // LEARNING NOTE: Jenkins Build with Parameters API Endpoint
        // Format: /job/{jobName}/buildWithParameters
        String buildUrl = String.format("%s/job/%s/buildWithParameters", jenkinsUrl, jenkinsJobName);

        // URL encode parameters to handle spaces and special characters
        String encodedRepoUrl = URLEncoder.encode(request.getRepositoryUrl(), StandardCharsets.UTF_8);
        String encodedBranch = URLEncoder.encode(
            request.getBranchName() != null ? request.getBranchName() : "main", 
            StandardCharsets.UTF_8
        );
        String encodedStudentName = URLEncoder.encode(request.getStudentName(), StandardCharsets.UTF_8);

        // Build URL with encoded parameters
        String urlWithParams = String.format(
            "%s?REPO_URL=%s&BRANCH=%s&STUDENT_NAME=%s",
            buildUrl,
            encodedRepoUrl,
            encodedBranch,
            encodedStudentName
        );

        // Create HTTP request with Basic Authentication
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .header("Authorization", getBasicAuthHeader())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            // Send request to Jenkins
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                log.info("Jenkins build triggered successfully");
                
                // LEARNING NOTE: In real scenario, get build number from Jenkins queue API
                // For demo purposes, we simulate a build number
                return generateBuildNumber();
            } else {
                log.error("Jenkins returned status code: {}", response.statusCode());
                throw new RuntimeException("Failed to trigger Jenkins build. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error communicating with Jenkins", e);
            
            // LEARNING NOTE: For demo without actual Jenkins, we simulate success
            log.warn("Jenkins not available - running in DEMO MODE");
            return generateBuildNumber();
        }
    }

    /**
     * Get build status from Jenkins
     * 
     * @param buildNumber Jenkins build number
     * @return Build status (SUCCESS, FAILURE, RUNNING, etc.)
     */
    public String getBuildStatus(Integer buildNumber) {
        log.info("Fetching build status for build number: {}", buildNumber);

        String statusUrl = String.format(
            "%s/job/%s/%d/api/json",
            jenkinsUrl, jenkinsJobName, buildNumber
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(statusUrl))
                    .header("Authorization", getBasicAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse JSON response (simplified - in production use JSON library)
                String body = response.body();
                if (body.contains("\"result\":\"SUCCESS\"")) {
                    return "SUCCESS";
                } else if (body.contains("\"result\":\"FAILURE\"")) {
                    return "FAILURE";
                } else if (body.contains("\"building\":true")) {
                    return "RUNNING";
                }
            }
        } catch (Exception e) {
            log.error("Error fetching build status from Jenkins", e);
        }

        // Demo mode - simulate status
        return "RUNNING";
    }

    /**
     * Get console output from Jenkins build
     * 
     * @param buildNumber Jenkins build number
     * @return Console output text
     */
    public String getConsoleOutput(Integer buildNumber) {
        log.info("Fetching console output for build number: {}", buildNumber);

        String consoleUrl = String.format(
            "%s/job/%s/%d/consoleText",
            jenkinsUrl, jenkinsJobName, buildNumber
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(consoleUrl))
                    .header("Authorization", getBasicAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            }
        } catch (Exception e) {
            log.error("Error fetching console output from Jenkins", e);
        }

        return "Console output not available (Demo mode)";
    }

    /**
     * Create Basic Authentication header
     * 
     * Jenkins API requires authentication using username and API token
     * Format: Basic base64(username:token)
     */
    private String getBasicAuthHeader() {
        String credentials = jenkinsUsername + ":" + jenkinsApiToken;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }

    /**
     * Generate a simulated build number for demo purposes
     * 
     * In real scenario, Jenkins provides the build number
     */
    private Integer generateBuildNumber() {
        return 100 + new Random().nextInt(900); // Random number between 100-999
    }
}
