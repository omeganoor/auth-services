package com.deloitte.demo.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@ToString
public class RoleData {

    private Long id;

    private String description;

    private RoleType roleName;
}