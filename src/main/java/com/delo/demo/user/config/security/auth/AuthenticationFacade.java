package com.delo.demo.user.config.security.auth;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();
}