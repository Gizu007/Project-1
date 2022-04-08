package com.revature.exception;

public class InvalidFileTypeException extends Exception {
    public InvalidFileTypeException() {
        super();
    }

    public InvalidFileTypeException(String message) {
        super(message);
    }

    public InvalidFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileTypeException(Throwable cause) {
        super(cause);
    }

    protected InvalidFileTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}