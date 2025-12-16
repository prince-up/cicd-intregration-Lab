package com.cicd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WEB CONFIGURATION
 * 
 * This configuration class handles Cross-Origin Resource Sharing (CORS).
 * 
 * LEARNING NOTE:
 * CORS allows the React frontend (running on port 3000) to call
 * the backend API (running on port 8080).
 * 
 * Without CORS configuration, browsers block these requests for security.
 */
@Configuration
public class WebConfig {

    @Value("${cors.allowed.origins:http://localhost:3000}")
    private String allowedOrigins;

    /**
     * Configure CORS mappings
     * 
     * This allows:
     * - React app on localhost:3000 to call backend APIs
     * - All HTTP methods (GET, POST, PUT, DELETE)
     * - All headers
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Apply to all /api/* endpoints
                        .allowedOrigins(allowedOrigins.split(",")) // Allow React app
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow all methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true) // Allow cookies
                        .maxAge(3600); // Cache preflight response for 1 hour
            }
        };
    }
}
