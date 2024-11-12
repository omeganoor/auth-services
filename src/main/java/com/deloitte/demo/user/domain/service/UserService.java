package com.deloitte.demo.user.domain.service;

import com.deloitte.demo.user.controller.dto.UserDto;

import java.util.List;

public interface UserService {

  UserDto saveUser (UserDto user);

  UserDto getUserById (Long id);

  List<UserDto> getAllUsers ();

  void deleteUser (Long id);

  UserDto assignRoleToUser (Long userId, String role);

  UserDto updateUser (UserDto user);

  boolean isCurrentUser (Long userId);

}