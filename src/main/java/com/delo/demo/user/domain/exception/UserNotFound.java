package com.delo.demo.user.domain.exception;

public class UserNotFound extends EmptyStackTrace {

    public UserNotFound () {
        super("user is not found");
    }

    public UserNotFound (String msg) {
        super(msg);
    }
}