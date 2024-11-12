package com.deloitte.demo.user.controller;


import com.deloitte.demo.user.controller.dto.ResponseDto;
import com.deloitte.demo.user.controller.dto.UserDto;
import com.deloitte.demo.user.domain.model.RoleType;
import com.deloitte.demo.user.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/account")
@Slf4j
public class UserController {

  private final UserService userService;

  public UserController (UserService userService) {
    this.userService = userService;
  }

  // Create or Update User (Only ADMIN role can access)
  @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseDto createUser (@RequestBody UserDto user) {
    // todo : assign default role user
    UserDto response = userService.saveUser(user);
    assignRoleToUser(response.getId(), RoleType.USER.name());
    response.setPassword(response.getPassword().replaceAll(".", "*"));

    return ResponseDto.success(response);
  }

  // Update User (Only USER role can access this for editing their own data)
  @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
  @PutMapping
  public ResponseDto updateUser (@RequestBody UserDto user) {
    UserDto response = userService.updateUser(user);
    response.setPassword(response.getPassword().replaceAll(".", "*"));
    return ResponseDto.success(response);
  }

  // Get User by ID (Only ADMIN and USER roles can access)
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/{id}")
  public ResponseDto getUserById (@PathVariable Long id) {
    log.info("getUserById({}) : ", id);

    UserDto response = userService.getUserById(id);
    response.setPassword(response.getPassword().replaceAll(".", "*"));
    return ResponseDto.success(response);
  }

//   Get All Users (Only ADMIN role can access)
  @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
  @GetMapping()
  public ResponseDto getAllUsers () {
    List<UserDto> response = userService.getAllUsers();
    response.forEach(r -> r.setPassword(r.getPassword().replaceAll(".", "*")));
    return ResponseDto.success(response);
  }

  // Delete User (Only ADMIN role can access)
  @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseDto deleteUser (@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseDto.noContent();
  }

  // Assign Role to User (Only ADMIN role can access)
  @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
  @PutMapping("/{userId}/assignRole")
  public ResponseDto assignRoleToUser (@PathVariable Long userId, @RequestParam String role) {
    UserDto response = userService.assignRoleToUser(userId, role);
    response.setPassword(response.getPassword().replaceAll(".", "*"));
    return ResponseDto.success(response);
  }

  // Update User (Only USER role can access this for editing their own data)
  @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
  @PutMapping("/update/{id}")
  public ResponseDto updateByUser (@PathVariable Long id, @RequestBody UserDto user) {
    user.setId(id);  // Set the ID to the path variable
    UserDto response = userService.updateUser(user);
    response.setPassword(response.getPassword().replaceAll(".", "*"));
    return ResponseDto.success(response);
  }

  @GetMapping("/info")
  public ResponseDto info () {
    return ResponseDto.success("running!!!");
  }

}