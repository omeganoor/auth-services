package com.delo.demo.user.domain.service;

import com.delo.demo.user.controller.dto.UserDto;
import com.delo.demo.user.domain.model.TokenClaims;

public interface AuthService {
  UserDto CreateUser (UserDto userDto);

  String loginUser(String username, String password);

  TokenClaims validateToken (String token);
}