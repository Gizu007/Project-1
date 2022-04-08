package com.revature.exception;

public class ReimbursementDoesNotExist extends Exception{
    public ReimbursementDoesNotExist() {
        super();
    }

    public ReimbursementDoesNotExist(String message) {
        super(message);
    }

    public ReimbursementDoesNotExist(String message, Throwable cause) {
        super(message, cause);
    }

    public ReimbursementDoesNotExist(Throwable cause) {
        super(cause);
    }

    protected ReimbursementDoesNotExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}