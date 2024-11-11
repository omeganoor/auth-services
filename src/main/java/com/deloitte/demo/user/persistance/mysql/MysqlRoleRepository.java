package com.deloitte.demo.user.persistance.mysql;

import com.deloitte.demo.user.domain.model.RoleType;
import com.deloitte.demo.user.persistance.mysql.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MysqlRoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRoleName (RoleType name);

}