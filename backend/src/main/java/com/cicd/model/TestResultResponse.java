package com.cicd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TEST RESULT RESPONSE DTO
 * 
 * This class represents test result details sent to frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponse {

    /**
     * Test ID
     */
    private Long id;

    /**
     * Test class name
     */
    private String testClass;

    /**
     * Test method name
     */
    private String testMethod;

    /**
     * Test status: PASSED, FAILED, SKIPPED
     */
    private String status;

    /**
     * Duration in milliseconds
     */
    private Long durationMs;

    /**
     * Error message if failed
     */
    private String errorMessage;
}
