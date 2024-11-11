package com.deloitte.demo.user.domain.exception;

public class InvalidPassword extends EmptyStackTrace {

    public InvalidPassword () {
        super("Invalid credentials");
    }
}