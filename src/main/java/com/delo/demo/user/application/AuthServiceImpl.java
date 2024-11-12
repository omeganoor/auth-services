package com.delo.demo.user.application;


import com.delo.demo.user.controller.dto.Mapper;
import com.delo.demo.user.controller.dto.UserDto;
import com.delo.demo.user.domain.exception.DuplicateAccountException;
import com.delo.demo.user.domain.exception.InvalidPassword;
import com.delo.demo.user.domain.exception.UserNotFound;
import com.delo.demo.user.domain.model.RoleData;
import com.delo.demo.user.domain.model.RoleType;
import com.delo.demo.user.domain.model.TokenClaims;
import com.delo.demo.user.domain.model.UserData;
import com.delo.demo.user.domain.repository.RoleRepository;
import com.delo.demo.user.domain.repository.UserRepository;
import com.delo.demo.user.domain.service.AuthService;
import com.delo.demo.user.domain.security.JwtConfiguration;
import com.delo.demo.user.persistance.mysql.MysqlUserRoleRepository;
import com.delo.demo.user.persistance.mysql.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
  private UserRepository userRepository;
  private JwtConfiguration jwtConfiguration;
  private RoleRepository roleRepository;
  private MysqlUserRoleRepository userRoleRepository;

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AuthServiceImpl (UserRepository userRepository, JwtConfiguration jwtConfiguration, RoleRepository roleRepository, MysqlUserRoleRepository userRoleRepository) {
    this.userRepository = userRepository;
    this.jwtConfiguration = jwtConfiguration;
    this.roleRepository = roleRepository;
    this.userRoleRepository = userRoleRepository;
  }


  public UserDto CreateUser (UserDto userDto) {
    // Check if user already exists
    if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
      throw new DuplicateAccountException("Username already taken");
    }

    // Check if email already exists
    if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
      throw new DuplicateAccountException();
    }

    // Encrypt password
    String passwordHash = passwordEncoder.encode(userDto.getPassword());

    // Create new user
    UserData user = UserData.builder()
        .username(userDto.getUsername())
        .email(userDto.getEmail())
        .username(userDto.getUsername())
        .password(passwordHash)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
    UserData response = userRepository.save(user);
    RoleData role = roleRepository.findByRoleName(RoleType.USER)
        .orElseThrow(() -> new UserNotFound("Role not found"));
    userRoleRepository.save(UserRole.builder()
        .userId(response.getId())
        .roleId(role.getId())
        .build());
    return Mapper.toUserDTO(response);
  }

  public String loginUser (String username, String password) {
    // Find the user by username
    UserData user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFound());

    // Verify password
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new InvalidPassword();
    }

    // Generate JWT token
    return jwtConfiguration.generateToken(user);
  }

  @Override
  public TokenClaims validateToken (String token) {
    TokenClaims tokenClaims = jwtConfiguration.validateToken(token);
    UserData user = userRepository.findByUsername(tokenClaims.getUsername())
        .orElseThrow(UserNotFound::new);
    tokenClaims.setRoles(user.getRoles());
    return tokenClaims;
  }
}