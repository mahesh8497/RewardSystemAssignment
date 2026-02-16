package com.rewardSystem.exception;

/**
 * Exception thrown when data processing fails.
 * This typically maps to HTTP 400 Bad Request or 422 Unprocessable Entity responses.
 */
public class DataProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataProcessingException(String message) {
        super(message);
    }

    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

