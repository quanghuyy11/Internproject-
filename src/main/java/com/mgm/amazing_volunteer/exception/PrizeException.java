package com.mgm.amazing_volunteer.exception;

public class PrizeException extends  RuntimeException{
    public PrizeException() {
        super();
    }

    public PrizeException(String message) {
        super(message);
    }

    public PrizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrizeException(Throwable cause) {
        super(cause);
    }

    protected PrizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
