package com.mgm.amazing_volunteer.exception;

public class EmptyFieldException extends RuntimeException {
    public EmptyFieldException() {
        super();
    }

    public EmptyFieldException(String message) {
        super(message);
    }

    public EmptyFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyFieldException(Throwable cause) {
        super(cause);
    }

    protected EmptyFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
