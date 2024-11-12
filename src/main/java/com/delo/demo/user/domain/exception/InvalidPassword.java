package com.delo.demo.user.domain.exception;

public class InvalidPassword extends EmptyStackTrace {

    public InvalidPassword () {
        super("Invalid credentials");
    }

    public InvalidPassword (String msg) {
        super(msg);
    }
}