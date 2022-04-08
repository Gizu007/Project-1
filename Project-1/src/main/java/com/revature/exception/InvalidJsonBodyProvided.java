package com.revature.exception;

public class InvalidJsonBodyProvided extends Exception{
    public InvalidJsonBodyProvided() {
        super();
    }

    public InvalidJsonBodyProvided(String message) {
        super(message);
    }

    public InvalidJsonBodyProvided(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJsonBodyProvided(Throwable cause) {
        super(cause);
    }

    protected InvalidJsonBodyProvided(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}