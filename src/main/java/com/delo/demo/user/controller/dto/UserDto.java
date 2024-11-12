package com.delo.demo.user.controller.dto;

import com.delo.demo.user.domain.model.RoleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    @Valid
    @NotNull(message = "password is required")
    private String password;

    @Valid
    @NotNull(message = "username is required")
    private String username;

    @Valid
    @NotNull(message = "email is required")
    private String email;

    List<RoleType> roles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}