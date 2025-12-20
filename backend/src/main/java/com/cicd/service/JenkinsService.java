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

        // Get Jenkins Crumb for CSRF protection
        String[] crumb = getCrumb();
        log.info("CSRF Crumb: {}", crumb != null ? crumb[0] + "=" + crumb[1] : "null");
        
        // Create HTTP request with Basic Authentication and CSRF token
        String authHeader = getBasicAuthHeader();
        log.info("Auth header: {}", authHeader.substring(0, Math.min(20, authHeader.length())) + "...");
        log.info("Triggering Jenkins at URL: {}", urlWithParams);
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.noBody());
        
        // Add CSRF crumb if available
        if (crumb != null) {
            log.info("Adding CSRF crumb header: {} = {}", crumb[0], crumb[1]);
            requestBuilder.header(crumb[0], crumb[1]);
        } else {
            log.warn("No CSRF crumb available - request might fail!");
        }
        
        HttpRequest httpRequest = requestBuilder.build();

        try {
            // Send request to Jenkins
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                log.info("Jenkins build triggered successfully");
                
                // Get queue item location from response header
                String queueLocation = response.headers().firstValue("Location").orElse(null);
                
                if (queueLocation != null) {
                    // Wait and get actual build number from queue
                    return waitForBuildNumber(queueLocation);
                } else {
                    // Fallback: get next build number
                    return getNextBuildNumber();
                }
            } else {
                log.error("Jenkins returned status code: {}", response.statusCode());
                log.error("Jenkins response body: {}", response.body());
                log.error("Jenkins response headers: {}", response.headers());
                throw new RuntimeException("Failed to trigger Jenkins build. Status: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            log.error("Error communicating with Jenkins", e);
            throw new RuntimeException("Failed to trigger Jenkins build: " + e.getMessage());
        }
    }

    /**
     * Get build status from Jenkins
     * 
     * @param buildNumber Jenkins build number
     * @return Build status (SUCCESS, FAILURE, RUNNING, etc.)
     */
    public String getBuildStatus(Integer buildNumber) {
        log.debug("Fetching build status for build number: {}", buildNumber);
        System.out.println("DEBUG: Fetching status for build " + buildNumber);

        String statusUrl = String.format(
            "%s/job/%s/%d/api/json",
            jenkinsUrl, jenkinsJobName, buildNumber
        );
        System.out.println("DEBUG: Status URL: " + statusUrl);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(statusUrl))
                    .header("Authorization", getBasicAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG: Response Code: " + response.statusCode());

            if (response.statusCode() == 200) {
                String body = response.body();
                System.out.println("DEBUG: Response Body: " + body);
                // Parse JSON using Jackson
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(body);
                
                boolean isBuilding = root.path("building").asBoolean(false);
                String result = root.path("result").asText(null);
                
                if (isBuilding) {
                    System.out.println("DEBUG: Status is RUNNING");
                    return "RUNNING";
                }
                
                if (result != null && !result.equals("null")) {
                    System.out.println("DEBUG: Status is " + result);
                    return result; // SUCCESS, FAILURE, ABORTED, etc.
                }
                
                // If not building and no result, likely undefined or just finished
                System.out.println("DEBUG: Status indeterminate -> RUNNING");
                return "RUNNING";
            } else {
                log.warn("Jenkins returned non-200 status for build {}: {}", buildNumber, response.statusCode());
                System.out.println("DEBUG: Non-200 Status: " + response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error fetching build status from Jenkins", e);
            System.out.println("DEBUG: Exception: " + e.getMessage());
            e.printStackTrace();
        }

        // Keep RUNNING if we can't determine status (temporary network blip?)
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
     * Get Jenkins Crumb for CSRF protection
     * Returns [headerName, headerValue] or null if not available
     */
    private String[] getCrumb() {
        try {
            String crumbUrl = jenkinsUrl + "/crumbIssuer/api/json";
            log.info("Fetching CSRF crumb from: {}", crumbUrl);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(crumbUrl))
                .header("Authorization", getBasicAuthHeader())
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Crumb response status: {}", response.statusCode());
            
            if (response.statusCode() == 200) {
                String body = response.body();
                log.debug("Crumb response body: {}", body);
                // Parse JSON: {"crumb":"xxx","crumbRequestField":"Jenkins-Crumb"}
                String crumbValue = extractJsonValue(body, "crumb");
                String crumbField = extractJsonValue(body, "crumbRequestField");
                
                if (crumbValue != null && crumbField != null) {
                    log.info("✓ Got Jenkins crumb: {} = {}", crumbField, crumbValue.substring(0, Math.min(10, crumbValue.length())) + "...");
                    return new String[]{crumbField, crumbValue};
                } else {
                    log.error("Failed to parse crumb from response: {}", body);
                }
            } else {
                log.error("Failed to get crumb. Status: {}, Body: {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("Could not get Jenkins crumb: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Simple JSON value extractor
     */
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int start = json.indexOf(searchKey);
        if (start == -1) return null;
        start += searchKey.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
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

    /**
     * Wait for build to start and get actual build number from Jenkins queue
     */
    private Integer waitForBuildNumber(String queueLocation) throws Exception {
        log.info("Waiting for build number from queue: {}", queueLocation);
        
        // Try to get build number from queue for up to 10 seconds
        for (int i = 0; i < 20; i++) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(queueLocation + "api/json"))
                    .header("Authorization", getBasicAuthHeader())
                    .GET()
                    .build();
                
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    String body = response.body();
                    // Parse JSON to get executable.number
                    if (body.contains("\"executable\"")) {
                        int start = body.indexOf("\"number\":") + 9;
                        int end = body.indexOf(",", start);
                        if (end == -1) end = body.indexOf("}", start);
                        String numberStr = body.substring(start, end).trim();
                        Integer buildNumber = Integer.parseInt(numberStr);
                        log.info("Got build number from queue: {}", buildNumber);
                        return buildNumber;
                    }
                }
                
                Thread.sleep(500); // Wait 500ms before retry
            } catch (Exception e) {
                log.debug("Queue check attempt {} failed: {}", i + 1, e.getMessage());
            }
        }
        
        // Fallback if queue check fails
        log.warn("Could not get build number from queue, using next build number");
        return getNextBuildNumber();
    }

    /**
     * Get next build number from Jenkins job
     */
    private Integer getNextBuildNumber() throws Exception {
        String jobUrl = String.format("%s/job/%s/api/json", jenkinsUrl, jenkinsJobName);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(jobUrl))
            .header("Authorization", getBasicAuthHeader())
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            String body = response.body();
            // Parse nextBuildNumber from JSON
            if (body.contains("\"nextBuildNumber\":")) {
                int start = body.indexOf("\"nextBuildNumber\":") + 18;
                int end = body.indexOf(",", start);
                if (end == -1) end = body.indexOf("}", start);
                String numberStr = body.substring(start, end).trim();
                Integer nextNumber = Integer.parseInt(numberStr);
                log.info("Next build number: {}", nextNumber);
                return nextNumber;
            }
        }
        
        throw new RuntimeException("Could not determine build number from Jenkins");
    }
}
