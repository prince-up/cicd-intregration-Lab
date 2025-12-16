package com.cicd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * APPLICATION CONTEXT TEST
 * 
 * This test ensures that the Spring Boot application context loads successfully.
 * It's a smoke test to catch major configuration errors.
 * 
 * LEARNING NOTE:
 * If this test fails, it usually means there's a problem with:
 * - Missing dependencies in pom.xml
 * - Configuration errors in application.properties
 * - Syntax errors in Spring components
 */
@SpringBootTest
class CicdApplicationTests {

    /**
     * Test: Application context loads
     * 
     * This test verifies that Spring can start the application
     * and wire all beans correctly.
     */
    @Test
    void contextLoads() {
        // If this test passes, Spring Boot started successfully
        // No assertions needed - the test passes if context loads
    }
}
