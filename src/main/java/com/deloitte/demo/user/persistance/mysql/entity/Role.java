package com.deloitte.demo.user.persistance.mysql.entity;

import com.deloitte.demo.user.domain.model.RoleType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    private static final long serialVersionUID = -915074092244502792L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;


    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private RoleType roleName;


//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;
}