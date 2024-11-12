package com.delo.demo.user.persistance;

import com.delo.demo.user.domain.model.RoleType;
import com.delo.demo.user.domain.model.UserData;
import com.delo.demo.user.domain.repository.UserRepository;
import com.delo.demo.user.persistance.mysql.MysqlRoleRepository;
import com.delo.demo.user.persistance.mysql.MysqlUserRepository;
import com.delo.demo.user.persistance.mysql.MysqlUserRoleRepository;
import com.delo.demo.user.persistance.mysql.entity.User;
import com.delo.demo.user.persistance.mysql.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final MysqlUserRepository repository;
    private final MysqlUserRoleRepository userRoleRepository;
    private final MysqlRoleRepository roleRepository;

    public UserRepositoryImpl (MysqlUserRepository repository, MysqlUserRoleRepository userRoleRepository, MysqlRoleRepository roleRepository) {
        this.repository = repository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<UserData> findByUserId(Long userId) {
//        log.info("findByUserId({})", userId);
        return repository.findById(userId)
                .map(p -> toDomain(p));
    }

    @Override
    public UserData save (UserData data) {
//        log.info("UserRepositoryImpl.create({})", data);

        User user = User.builder()
                .passwordHash(data.getPassword())
                .username(data.getUsername())
                .email(data.getEmail())
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .build();
        if (data.getId()!= null){
            user.setId(data.getId());
        }
        User response = repository.save(user);
        return toDomain(response);
    }

    @Override
    public Optional<UserData> findByUsername (String username) {
        return repository.findByUsername(username)
            .map(this::toDomain);
    }

    @Override
    public Optional<UserData> findByEmail (String email) {
        return repository.findByEmail(email)
            .map(this::toDomain);
    }

    @Override
    public Optional<UserData> findById (Long id) {
        return repository.findById(id)
            .map(this::toDomain);
    }

    @Override
    public List<UserData> findAll () {
        List<User> users = repository.findAll();
        return users.stream().map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById (Long id) {
        List<UserRole> userRole = userRoleRepository.findAllByUserId(id);
        userRoleRepository.deleteAll(userRole);
        repository.deleteById(id);
    }

    private UserData toDomain (User data) {

        List<RoleType> roles = getRoles(data);
        return UserData.builder()
            .id(data.getId())
            .username(data.getUsername())
            .email(data.getEmail())
            .password(data.getPasswordHash())
            .roles(roles)
            .createdAt(data.getCreatedAt())
            .updatedAt(data.getUpdatedAt())
            .build();
    }

    private List<RoleType> getRoles (User data) {
        List<RoleType> roles = new ArrayList<>();
        List<UserRole> userRole = userRoleRepository.findAllByUserId(data.getId());
        for (UserRole ur : userRole) {
            RoleType role = roleRepository.getById(ur.getRoleId()).getRoleName();
            roles.add(role);
        }

        return roles;
    }

}