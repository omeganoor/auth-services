package com.deloitte.demo.user.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateAccountException extends EmptyStackTrace {
    public DuplicateAccountException (String message) {
        super(message.isEmpty() ? "account with this username or email is already exist" : message);
    }
}