
package com.deloitte.demo.user.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class TokenClaims {
    private boolean active;
    private String username;
    private long exp;
    private long iat;
    @JsonProperty("sub")
    private String subject;
    private String uid;
    @JsonProperty("roles")
    List<RoleType> roles;
}