package com.proj.mate.controller;

import com.proj.mate.dto.UserRequestDto;
import com.proj.mate.dto.UserResponseDto;
import com.proj.mate.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        try {
            UserResponseDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create user
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto createdUser = userService.createUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto updatedUserDto) {

        try {
            UserResponseDto user = userService.updateUser(id, updatedUserDto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update user profile picture URL (generated on client side)
    @PutMapping("/{id}/profile-pic")
    public ResponseEntity<UserResponseDto> updateProfilePicUrl(
            @PathVariable Long id,
            @RequestParam String url) {

        try {
            UserResponseDto updatedUser = userService.updateProfilePic(id, url);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsersByName(
            @RequestParam String name) {

        List<UserResponseDto> users = userService.getUsersByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(users);
    }

    // Get user by email
    @GetMapping("/search/email")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @RequestParam String email) {

        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get users by role
    @GetMapping("/search/role")
    public ResponseEntity<List<UserResponseDto>> getUsersByRole(
            @RequestParam String role) {

        List<UserResponseDto> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    // Get users by role IN
    @GetMapping("/search/role/in")
    public ResponseEntity<List<UserResponseDto>> getUsersByRoleIn(
            @RequestParam List<String> roles) {

        List<UserResponseDto> users = userService.getUsersByRoleIn(roles);
        return ResponseEntity.ok(users);
    }

    // Get users by role NOT IN
    @GetMapping("/search/role/not-in")
    public ResponseEntity<List<UserResponseDto>> getUsersByRoleNotIn(
            @RequestParam List<String> roles) {

        List<UserResponseDto> users = userService.getUsersByRoleNotIn(roles);
        return ResponseEntity.ok(users);
    }
}
