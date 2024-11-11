package com.deloitte.demo.user.domain.model;

public enum RoleType {
    ROOT("root"),
    ADMIN("admin"),
    USER("user")
    ;

    private final String value;

    RoleType(String value) {
        this.value = value;
    }
}