package com.mgm.amazing_volunteer.exception;

public class PrizeRequestException extends RuntimeException{
    public PrizeRequestException() {
        super();
    }

    public PrizeRequestException(String message) {
        super(message);
    }

    public PrizeRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrizeRequestException(Throwable cause) {
        super(cause);
    }

    protected PrizeRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
