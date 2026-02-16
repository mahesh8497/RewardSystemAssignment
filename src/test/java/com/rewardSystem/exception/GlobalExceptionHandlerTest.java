package com.rewardSystem.exception;

import com.rewardSystem.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for GlobalExceptionHandler.
 * Verifies that all exceptions are handled correctly with appropriate HTTP status codes
 * and meaningful error response messages.
 */
@DisplayName("Global Exception Handler Test Suite")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/v1/api/rewards");
        webRequest = new ServletWebRequest(httpServletRequest);
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with 404 status")
    void testHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Customer not found");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("NOT_FOUND", response.getBody().getError());
        assertEquals("Customer not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle DataProcessingException with 400 status")
    void testHandleDataProcessingException() {
        // Arrange
        DataProcessingException exception = new DataProcessingException("Error processing data");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataProcessingException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("BAD_REQUEST", response.getBody().getError());
        assertEquals("Error processing data", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle InternalServerException with 500 status")
    void testHandleInternalServerException() {
        // Arrange
        InternalServerException exception = new InternalServerException("Internal server error occurred");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInternalServerException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getError());
        assertEquals("Internal server error occurred", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with 400 status")
    void testHandleIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument provided");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("BAD_REQUEST", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Invalid argument"));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle NullPointerException with 500 status")
    void testHandleNullPointerException() {
        // Arrange
        NullPointerException exception = new NullPointerException("Null pointer occurred");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNullPointerException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getError());
        assertEquals("An unexpected error occurred while processing your request", response.getBody().getMessage());
        assertEquals("Null pointer exception", response.getBody().getDetails());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle generic Exception with 500 status")
    void testHandleGenericException() {
        // Arrange
        Exception exception = new Exception("Some unexpected error");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getError());
        assertEquals("An unexpected error occurred while processing your request", response.getBody().getMessage());
        assertEquals("Some unexpected error", response.getBody().getDetails());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should include path in error response")
    void testErrorResponseIncludesPath() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getPath());
    }

    @Test
    @DisplayName("Should handle exception with cause")
    void testHandleExceptionWithCause() {
        // Arrange
        Throwable cause = new IllegalStateException("Illegal state");
        DataProcessingException exception = new DataProcessingException("Processing failed", cause);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataProcessingException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
    }

    @Test
    @DisplayName("Should validate ErrorResponse structure")
    void testErrorResponseStructure() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test error");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);
        ErrorResponse errorResponse = response.getBody();

        // Assert
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getTimestamp());
        assertTrue(errorResponse.getStatus() > 0);
        assertNotNull(errorResponse.getError());
        assertNotNull(errorResponse.getMessage());
        assertNotNull(errorResponse.getPath());
        // Details can be null for some exceptions
    }

    @Test
    @DisplayName("Should not throw exception while handling exception")
    void testNoExceptionWhileHandling() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        // Act & Assert
        assertDoesNotThrow(() -> {
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);
            assertNotNull(response);
        });
    }
}

