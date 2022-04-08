package com.revature.exception;

public class UserDoesNotExist extends Exception{
    public UserDoesNotExist() {
        super();
    }

    public UserDoesNotExist(String message) {
        super(message);
    }

    public UserDoesNotExist(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesNotExist(Throwable cause) {
        super(cause);
    }

    protected UserDoesNotExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}