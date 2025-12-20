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
    public org.springframework.web.filter.CorsFilter corsFilter() {
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        
        // Allow credentials (cookies, auth headers)
        config.setAllowCredentials(true);
        
        // ALLOW ALL ORIGINS using Check Pattern (Correct way for Spring Boot + Credentials)
        config.addAllowedOriginPattern("*"); 
        
        // Allow all headers and methods
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        source.registerCorsConfiguration("/**", config);
        return new org.springframework.web.filter.CorsFilter(source);
    }
}
