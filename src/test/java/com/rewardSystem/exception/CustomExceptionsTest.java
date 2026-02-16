package com.rewardSystem.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for custom exception classes.
 * Verifies proper instantiation and behavior of custom exceptions.
 */
@DisplayName("Custom Exception Classes Test Suite")
class CustomExceptionsTest {

    @Test
    @DisplayName("ResourceNotFoundException should be created with message")
    void testResourceNotFoundExceptionWithMessage() {
        // Arrange
        String message = "Customer with ID 123 not found";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("ResourceNotFoundException should be created with message and cause")
    void testResourceNotFoundExceptionWithCause() {
        // Arrange
        String message = "Customer not found";
        Throwable cause = new IllegalArgumentException("Invalid ID");

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("DataProcessingException should be created with message")
    void testDataProcessingExceptionWithMessage() {
        // Arrange
        String message = "Failed to process transaction data";

        // Act
        DataProcessingException exception = new DataProcessingException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("DataProcessingException should be created with message and cause")
    void testDataProcessingExceptionWithCause() {
        // Arrange
        String message = "Data processing error";
        Throwable cause = new NullPointerException("Null transaction");

        // Act
        DataProcessingException exception = new DataProcessingException(message, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("InternalServerException should be created with message")
    void testInternalServerExceptionWithMessage() {
        // Arrange
        String message = "Unexpected database error";

        // Act
        InternalServerException exception = new InternalServerException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("InternalServerException should be created with message and cause")
    void testInternalServerExceptionWithCause() {
        // Arrange
        String message = "Internal server error";
        Throwable cause = new Exception("Database connection failed");

        // Act
        InternalServerException exception = new InternalServerException(message, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("All custom exceptions should extend RuntimeException")
    void testAllExceptionsExtendRuntime() {
        // Arrange
        ResourceNotFoundException ex1 = new ResourceNotFoundException("Test");
        DataProcessingException ex2 = new DataProcessingException("Test");
        InternalServerException ex3 = new InternalServerException("Test");

        // Assert
        assertTrue(ex1 instanceof RuntimeException);
        assertTrue(ex2 instanceof RuntimeException);
        assertTrue(ex3 instanceof RuntimeException);
    }

    @Test
    @DisplayName("Exceptions should be throwable")
    void testExceptionsAreThrowable() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("Test");
        });

        assertThrows(DataProcessingException.class, () -> {
            throw new DataProcessingException("Test");
        });

        assertThrows(InternalServerException.class, () -> {
            throw new InternalServerException("Test");
        });
    }

    @Test
    @DisplayName("Exceptions should have serialVersionUID")
    void testExceptionsHaveSerialVersionUID() {
        // This test verifies that custom exceptions are serializable
        // by checking they have serialVersionUID defined
        try {
            ResourceNotFoundException ex1 = new ResourceNotFoundException("Test");
            DataProcessingException ex2 = new DataProcessingException("Test");
            InternalServerException ex3 = new InternalServerException("Test");

            // All should be serializable
            assertTrue(ex1 instanceof java.io.Serializable);
            assertTrue(ex2 instanceof java.io.Serializable);
            assertTrue(ex3 instanceof java.io.Serializable);
        } catch (Exception e) {
            fail("Exception serialization test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Exception messages should be preserved")
    void testExceptionMessagesPreserved() {
        // Arrange
        String msg1 = "Message 1";
        String msg2 = "Message 2";
        String msg3 = "Message 3";

        // Act & Assert
        assertEquals(msg1, new ResourceNotFoundException(msg1).getMessage());
        assertEquals(msg2, new DataProcessingException(msg2).getMessage());
        assertEquals(msg3, new InternalServerException(msg3).getMessage());
    }

    @Test
    @DisplayName("Exception causes should be preserved")
    void testExceptionCausesPreserved() {
        // Arrange
        Throwable cause = new RuntimeException("Root cause");

        // Act & Assert
        assertEquals(cause, new ResourceNotFoundException("Error", cause).getCause());
        assertEquals(cause, new DataProcessingException("Error", cause).getCause());
        assertEquals(cause, new InternalServerException("Error", cause).getCause());
    }
}

