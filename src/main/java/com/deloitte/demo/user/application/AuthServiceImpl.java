package com.deloitte.demo.user.application;


import com.deloitte.demo.user.controller.dto.Mapper;
import com.deloitte.demo.user.controller.dto.UserDto;
import com.deloitte.demo.user.domain.exception.DuplicateAccountException;
import com.deloitte.demo.user.domain.exception.InvalidPassword;
import com.deloitte.demo.user.domain.exception.UserNotFound;
import com.deloitte.demo.user.domain.model.TokenClaims;
import com.deloitte.demo.user.domain.model.UserData;
import com.deloitte.demo.user.domain.repository.UserRepository;
import com.deloitte.demo.user.domain.service.AuthService;
import com.deloitte.demo.user.domain.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
  private UserRepository userRepository;
  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AuthServiceImpl (UserRepository userRepository ) {
    this.userRepository = userRepository;
  }


  public UserDto CreateUser (UserDto userDto) {
    // Check if user already exists
    if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
      throw new DuplicateAccountException("Username already taken");
    }

    // Check if email already exists
    if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
      throw new DuplicateAccountException("");
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
    return JwtUtil.generateToken(user);
  }

  @Override
  public TokenClaims validateToken (String token) {
    TokenClaims tokenClaims = JwtUtil.validateToken(token);
    UserData user = userRepository.findByUsername(tokenClaims.getUsername())
        .orElseThrow(UserNotFound::new);
    tokenClaims.setRoles(user.getRoles());
    return tokenClaims;
  }
}