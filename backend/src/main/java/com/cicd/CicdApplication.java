package com.cicd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * STUDENT CI/CD LAB - MAIN APPLICATION CLASS
 * 
 * This is the entry point of the Spring Boot application.
 * The @SpringBootApplication annotation enables:
 * - Auto-configuration
 * - Component scanning
 * - Configuration properties
 * 
 * @EnableScheduling enables automatic polling of Jenkins build status
 * 
 * @author Student CI/CD Lab Team
 */
@SpringBootApplication
@EnableScheduling
public class CicdApplication {

    /**
     * Main method - starts the Spring Boot application
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CicdApplication.class, args);
        System.out.println("========================================");
        System.out.println("Student CI/CD Lab Application Started!");
        System.out.println("Backend API: http://localhost:8080");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
