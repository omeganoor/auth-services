package com.delo.demo.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class RoleData {

    private Long id;

    private String description;

    private RoleType roleName;
}