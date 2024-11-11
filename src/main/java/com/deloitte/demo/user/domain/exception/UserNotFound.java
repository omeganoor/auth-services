package com.deloitte.demo.user.domain.exception;

public class UserNotFound extends EmptyStackTrace {

    public UserNotFound () {
        super("user is not found");
    }
}