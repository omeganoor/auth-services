package com.deloitte.demo.user.config.security.auth;

import com.deloitte.demo.user.domain.model.RoleType;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.AuthenticatedPrincipal;

@Data
@Builder
public class AuthUser implements AuthenticatedPrincipal {
    private Long clientId;
    private Long userId;
    private String fullName;
    private String email;
    private RoleType roleType;
    private String token;

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}