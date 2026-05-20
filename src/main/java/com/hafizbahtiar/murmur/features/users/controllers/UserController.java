package com.hafizbahtiar.murmur.features.users.controllers;

import com.hafizbahtiar.murmur.common.dto.ApiResponse;
import com.hafizbahtiar.murmur.common.util.ResponseUtil;
import com.hafizbahtiar.murmur.features.users.dto.UserRegistrationRequest;
import com.hafizbahtiar.murmur.features.users.dto.UserResponse;
import com.hafizbahtiar.murmur.features.users.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseUtil.ok(userService.getUsers(pageable));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        return ResponseUtil.created(userService.register(request), "User registered successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        return ResponseUtil.ok(userService.getById(id));
    }

    @GetMapping("/by-uuid/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUuid(@PathVariable UUID uuid) {
        return ResponseUtil.ok(userService.getByUuid(uuid));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        return ResponseUtil.ok(userService.getByEmail(email));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        return ResponseUtil.ok(userService.getByUsername(username));
    }
}
