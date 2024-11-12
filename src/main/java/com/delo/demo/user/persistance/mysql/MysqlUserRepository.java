package com.delo.demo.user.persistance.mysql;

import com.delo.demo.user.persistance.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MysqlUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername (String username);

    Optional<User> findByEmail (String email);
}