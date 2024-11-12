package com.delo.demo.user.persistance;

import com.delo.demo.user.domain.model.RoleData;
import com.delo.demo.user.domain.model.RoleType;
import com.delo.demo.user.domain.repository.RoleRepository;
import com.delo.demo.user.persistance.mysql.MysqlRoleRepository;
import com.delo.demo.user.persistance.mysql.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class RoleRepositoryImpl implements RoleRepository {

    private final MysqlRoleRepository roleRepository;

    public RoleRepositoryImpl (MysqlRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private RoleData toDomain (Role data) {
        log.info("toDomain({})", data);

        return RoleData.builder()
            .id(data.getId())
            .roleName(data.getRoleName())
            .description(data.getDescription())
            .build();
    }

    @Override
    public Optional<RoleData> findByRoleName (RoleType name) {
        return roleRepository.findByRoleName(name)
            .map(this::toDomain);
    }
}