package com.delo.demo.user.domain.repository;

import com.delo.demo.user.domain.model.RoleData;
import com.delo.demo.user.domain.model.RoleType;

import java.util.Optional;

public interface RoleRepository {
    Optional<RoleData> findByRoleName (RoleType name);

}