package com.cicd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
@Slf4j
public class NotificationService {

    @Value("${discord.webhook.url:}")
    private String discordWebhookUrl;

    private final HttpClient httpClient;

    public NotificationService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void sendNotification(String message) {
        // ALWAYS log to system console/logs (The "console simulation")
        log.info("🔔 NOTIFICATION: {}", message);

        // If Discord webhook is configured, send it there too
        if (discordWebhookUrl != null && !discordWebhookUrl.isEmpty()) {
            sendDiscordNotification(message);
        }
    }

    private void sendDiscordNotification(String message) {
        try {
            // Simple JSON payload for Discord
            String jsonPayload = String.format("{\"content\": \"%s\"}", message);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(discordWebhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() >= 200 && response.statusCode() < 300) {
                            log.debug("Discord notification sent successfully");
                        } else {
                            log.warn("Failed to send Discord notification: {}", response.statusCode());
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending Discord notification", e);
        }
    }
    
    public void sendPipelineStart(String studentName, String repoUrl) {
        String msg = String.format("🚀 **Pipeline Started!**\nStudent: %s\nRepo: %s", studentName, repoUrl);
        sendNotification(msg);
    }

    public void sendPipelineCompletion(String studentName, String status, Long buildNumber) {
        String emoji = status.equalsIgnoreCase("SUCCESS") ? "✅" : "❌";
        String msg = String.format("%s **Pipeline Finished**\nStudent: %s\nBuild #%d\nStatus: %s", emoji, studentName, buildNumber, status);
        sendNotification(msg);
    }
}
