package com.revature.exception;

public class InvalidQueryParamProvided extends Exception{
    public InvalidQueryParamProvided() {
        super();
    }

    public InvalidQueryParamProvided(String message) {
        super(message);
    }

    public InvalidQueryParamProvided(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidQueryParamProvided(Throwable cause) {
        super(cause);
    }

    protected InvalidQueryParamProvided(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}