package com.logiflow.exception;

/**
 * Base checked exception for the LogiFlow logistics domain.
 */
public class LogisticsException extends Exception {
    public LogisticsException(String message) {
        super(message);
    }

    public LogisticsException(String message, Throwable cause) {
        super(message, cause);
    }
}
