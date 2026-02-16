package com.rewardSystem.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ErrorResponse DTO.
 * Verifies proper creation, serialization, and data handling of error responses.
 */
@DisplayName("ErrorResponse DTO Test Suite")
class ErrorResponseTest {

    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        errorResponse = new ErrorResponse();
    }

    @Test
    @DisplayName("Should create ErrorResponse with default constructor")
    void testDefaultConstructor() {
        // Assert
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Should create ErrorResponse with status, error, message, and path")
    void testConstructorWithParams() {
        // Arrange
        int status = 404;
        String error = "NOT_FOUND";
        String message = "Resource not found";
        String path = "/v1/api/rewards";

        // Act
        ErrorResponse response = new ErrorResponse(status, error, message, path);

        // Assert
        assertNotNull(response);
        assertEquals(status, response.getStatus());
        assertEquals(error, response.getError());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create ErrorResponse with details")
    void testConstructorWithDetails() {
        // Arrange
        int status = 500;
        String error = "INTERNAL_SERVER_ERROR";
        String message = "An error occurred";
        String path = "/v1/api/rewards";
        String details = "Database connection failed";

        // Act
        ErrorResponse response = new ErrorResponse(status, error, message, path, details);

        // Assert
        assertNotNull(response);
        assertEquals(status, response.getStatus());
        assertEquals(error, response.getError());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertEquals(details, response.getDetails());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should set and get all properties")
    void testSettersAndGetters() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 400;
        String error = "BAD_REQUEST";
        String message = "Invalid request";
        String path = "/v1/api/test";
        String details = "Missing required fields";

        // Act
        errorResponse.setTimestamp(timestamp);
        errorResponse.setStatus(status);
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        errorResponse.setPath(path);
        errorResponse.setDetails(details);

        // Assert
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    @DisplayName("Should have non-null timestamp after creation")
    void testTimestampNotNull() {
        // Act
        ErrorResponse response = new ErrorResponse();

        // Assert
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should generate meaningful toString()")
    void testToString() {
        // Arrange
        ErrorResponse response = new ErrorResponse(404, "NOT_FOUND", "Not found", "/api/test");

        // Act
        String toString = response.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("ErrorResponse"));
        assertTrue(toString.contains("status"));
        assertTrue(toString.contains("error"));
        assertTrue(toString.contains("message"));
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        // Act
        errorResponse.setDetails(null);
        errorResponse.setError(null);
        errorResponse.setMessage(null);

        // Assert
        assertNull(errorResponse.getDetails());
        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Act
        errorResponse.setError("");
        errorResponse.setMessage("");
        errorResponse.setPath("");
        errorResponse.setDetails("");

        // Assert
        assertEquals("", errorResponse.getError());
        assertEquals("", errorResponse.getMessage());
        assertEquals("", errorResponse.getPath());
        assertEquals("", errorResponse.getDetails());
    }

    @Test
    @DisplayName("Should handle special characters in messages")
    void testSpecialCharacters() {
        // Arrange
        String specialMessage = "Error: Invalid @#$%^&*() characters!";

        // Act
        errorResponse.setMessage(specialMessage);

        // Assert
        assertEquals(specialMessage, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Should handle long messages")
    void testLongMessages() {
        // Arrange
        String longMessage = "A".repeat(500);

        // Act
        errorResponse.setMessage(longMessage);

        // Assert
        assertEquals(longMessage, errorResponse.getMessage());
        assertEquals(500, errorResponse.getMessage().length());
    }

    @Test
    @DisplayName("Should maintain data integrity through multiple operations")
    void testDataIntegrity() {
        // Arrange
        int status = 404;
        String error = "NOT_FOUND";
        String message = "Resource not found";
        String path = "/api/endpoint";

        // Act
        ErrorResponse response = new ErrorResponse(status, error, message, path);

        // Modify and verify
        response.setStatus(500);
        response.setError("INTERNAL_SERVER_ERROR");

        // Assert
        assertEquals(500, response.getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", response.getError());
        // Original values should not affect
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
    }

    @Test
    @DisplayName("Should be compatible with different HTTP status codes")
    void testDifferentHttpStatusCodes() {
        // Test various HTTP status codes
        int[] statusCodes = {400, 401, 403, 404, 405, 500, 502, 503};

        for (int statusCode : statusCodes) {
            ErrorResponse response = new ErrorResponse();
            response.setStatus(statusCode);
            assertEquals(statusCode, response.getStatus());
        }
    }
}

