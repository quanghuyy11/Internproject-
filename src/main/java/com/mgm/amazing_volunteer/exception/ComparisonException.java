package com.mgm.amazing_volunteer.exception;

public class ComparisonException extends RuntimeException {
    public ComparisonException() {
        super();
    }

    public ComparisonException(String message) {
        super(message);
    }

    public ComparisonException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComparisonException(Throwable cause) {
        super(cause);
    }

    protected ComparisonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
