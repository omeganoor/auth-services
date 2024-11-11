package com.deloitte.demo.user.domain.repository;

import com.deloitte.demo.user.domain.model.RoleData;
import com.deloitte.demo.user.domain.model.RoleType;

import java.util.Optional;

public interface RoleRepository {
    Optional<RoleData> findByRoleName (RoleType name);

}