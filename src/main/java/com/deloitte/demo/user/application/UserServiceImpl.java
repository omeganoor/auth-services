package com.deloitte.demo.user.application;

import com.deloitte.demo.user.controller.dto.Mapper;
import com.deloitte.demo.user.controller.dto.UserDto;
import com.deloitte.demo.user.domain.exception.DuplicateAccountException;
import com.deloitte.demo.user.domain.exception.UserNotFound;
import com.deloitte.demo.user.domain.model.RoleData;
import com.deloitte.demo.user.domain.model.RoleType;
import com.deloitte.demo.user.domain.model.UserData;
import com.deloitte.demo.user.domain.repository.RoleRepository;
import com.deloitte.demo.user.domain.repository.UserRepository;
import com.deloitte.demo.user.domain.service.UserService;
import com.deloitte.demo.user.persistance.mysql.MysqlUserRoleRepository;
import com.deloitte.demo.user.persistance.mysql.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MysqlUserRoleRepository userRoleRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl (UserRepository userRepository, RoleRepository roleRepository, MysqlUserRoleRepository userRoleRepository){
        this.userRepository=userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public boolean isCurrentUser (Long userId) {
        UserData currentUser = getCurrentAuthenticatedUser();
        return currentUser.getId().equals(userId);
    }

    // Method to get the current authenticated user
    public UserData getCurrentAuthenticatedUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserData) authentication.getPrincipal();
    }

    @Transactional
    public UserDto saveUser (UserDto userDto) {

        // Check if email & username already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateAccountException("");
        }
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new DuplicateAccountException("Username already taken");
        }

        UserData user = UserData.builder()
            .username(userDto.getUsername())
            .email(userDto.getEmail())
            .username(userDto.getUsername())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        UserData response = userRepository.save(user);
        return Mapper.toUserDTO(response);
    }

    @Transactional
    public UserDto updateUser (UserDto userDto) {

        // Check if email & username already exists
        UserData oldUser = userRepository.findById(userDto.getId())
            .orElseThrow(UserNotFound::new);
        oldUser.setId(userDto.getId());
        oldUser.setUsername(userDto.getUsername().isEmpty() ? oldUser.getUsername() : userDto.getUsername());
        oldUser.setEmail(userDto.getEmail().isEmpty() ? oldUser.getEmail() : userDto.getEmail());
        oldUser.setPassword((userDto.getPassword().isEmpty() ?
            oldUser.getPassword() : passwordEncoder.encode(userDto.getPassword())));
        oldUser.setUpdatedAt(LocalDateTime.now());
        UserData response = userRepository.save(oldUser);
        return Mapper.toUserDTO(response);
    }

    @Override
    public UserDto getUserById (Long id) {
        UserData response = userRepository.findById(id)
            .orElseThrow(UserNotFound::new);
        return Mapper.toUserDTO(response);
    }

    @Override
    public List<UserDto> getAllUsers () {
        List<UserData> response = userRepository.findAll();
        return response.stream()
            .map(Mapper::toUserDTO)
            .toList();
    }

    @Override
    @Transactional
    public void deleteUser (Long id) {
        userRepository.deleteById(id);
    }

    // Assign Role to User
    @Override
    @Transactional
    public UserDto assignRoleToUser (Long userId, String roleName) {
        RoleType roleType = RoleType.valueOf(roleName);
        RoleData role = roleRepository.findByRoleName(roleType)
            .orElseThrow(() -> new RuntimeException("Role not found"));
        userRoleRepository.save(UserRole.builder().userId(userId).roleId(role.getId()).build());

        return getUserById(userId);
    }
}