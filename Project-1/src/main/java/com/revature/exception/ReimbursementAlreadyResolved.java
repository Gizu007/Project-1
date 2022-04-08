package com.revature.exception;

public class ReimbursementAlreadyResolved extends Exception{
    public ReimbursementAlreadyResolved() {
        super();
    }

    public ReimbursementAlreadyResolved(String message) {
        super(message);
    }

    public ReimbursementAlreadyResolved(String message, Throwable cause) {
        super(message, cause);
    }

    public ReimbursementAlreadyResolved(Throwable cause) {
        super(cause);
    }

    protected ReimbursementAlreadyResolved(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}