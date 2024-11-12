package com.delo.demo.user.domain.exception;

public class EmptyStackTrace extends RuntimeException {

    protected String message;

    public EmptyStackTrace (String message) {
        super(message);
        this.message = message;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return null;
    }
}