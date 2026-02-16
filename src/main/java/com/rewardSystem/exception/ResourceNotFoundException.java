package com.rewardSystem.exception;

/**
 * Exception thrown when a requested resource is not found.
 * This typically maps to HTTP 404 Not Found responses.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

