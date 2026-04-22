package com.hafizbahtiar.murmur.features.users.controllers;

import com.hafizbahtiar.murmur.common.dto.ApiResponse;
import com.hafizbahtiar.murmur.common.util.ResponseUtil;
import com.hafizbahtiar.murmur.features.users.dto.UserRegistrationRequest;
import com.hafizbahtiar.murmur.features.users.dto.UserResponse;

import java.util.List;
import java.util.UUID;

import com.hafizbahtiar.murmur.features.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ResponseUtil.ok(users);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse response = userService.register(request);
        return ResponseUtil.created(response, "User registered successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse user = userService.getById(id);
        return ResponseUtil.ok(user);
    }

    @GetMapping("/by-uuid/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable UUID uuid) {
        UserResponse user = userService.getByUuid(uuid);
        return ResponseUtil.ok(user);
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        UserResponse user = userService.getByEmail(email);
        return ResponseUtil.ok(user);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        UserResponse user = userService.getByUsername(username);
        return ResponseUtil.ok(user);
    }
}
