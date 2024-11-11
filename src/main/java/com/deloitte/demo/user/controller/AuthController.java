package com.deloitte.demo.user.controller;


import com.deloitte.demo.user.controller.dto.ResponseDto;
import com.deloitte.demo.user.controller.dto.UserDto;
import com.deloitte.demo.user.domain.model.TokenClaims;
import com.deloitte.demo.user.domain.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {

  private AuthService authService;

  public AuthController (AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseDto registerUser (@RequestBody UserDto dto) {

    UserDto response = authService.CreateUser(dto);
    response.setPassword(response.getPassword().replaceAll(".", "*"));
    return ResponseDto.success(response);
  }

  // Login user and return JWT token
  @PostMapping("/login")
  public ResponseDto loginUser (@RequestParam String username,
                                @RequestParam String password) {
    String jwt = authService.loginUser(username, password);
    return ResponseDto.success(jwt);
  }

  // validate JWT token
  @GetMapping("/validate")
  public ResponseDto validateToken (@RequestParam String token) {
    TokenClaims claims = authService.validateToken(token);
    return ResponseDto.success(claims);
  }
}