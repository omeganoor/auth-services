package com.delo.demo.user.persistance.mysql;

import com.delo.demo.user.persistance.mysql.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MysqlUserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUserId (Long userId);


}