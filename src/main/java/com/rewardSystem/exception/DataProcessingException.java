package com.rewardSystem.exception;

public class DataProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataProcessingException(String message) {
        super(message);
    }

    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

