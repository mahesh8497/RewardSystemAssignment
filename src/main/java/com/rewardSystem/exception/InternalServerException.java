package com.rewardSystem.exception;

/**
 * Exception thrown for unexpected internal server errors.
 * This typically maps to HTTP 500 Internal Server Error responses.
 */
public class InternalServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

