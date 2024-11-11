package com.deloitte.demo.user.persistance.mysql;

import com.deloitte.demo.user.domain.model.RoleType;
import com.deloitte.demo.user.persistance.mysql.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MysqlUserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUserId (Long userId);


}