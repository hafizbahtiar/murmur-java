package com.hafizbahtiar.murmur.features.users.services;

import com.hafizbahtiar.murmur.features.users.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse register(UserRegistrationRequest request);

    UserResponse getById(Long id);

    UserResponse getByUuid(UUID uuid);

    UserResponse getByEmail(String email);

    UserResponse getByUsername(String username);

    Page<UserResponse> getUsers(Pageable pageable);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    boolean emailExists(String email);

    boolean usernameExists(String username);

    UserResponse updateUserRole(Long userId, String newRole);
}
