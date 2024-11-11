package com.deloitte.demo.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@ToString
public class UserData {
    private Long id;
    private String password;
    private String username;
    private String email;
    List<RoleType> roles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}