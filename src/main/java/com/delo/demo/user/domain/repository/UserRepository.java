package com.delo.demo.user.domain.repository;

import com.delo.demo.user.domain.model.UserData;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserData> findByUserId(Long userId);

    UserData save (UserData data);

    Optional<UserData> findByUsername (String username);

    Optional<UserData> findByEmail (String email);

    Optional<UserData> findById (Long id);

    List<UserData> findAll ();

    void deleteById (Long id);
}