package com.hafizbahtiar.murmur.features.users.services;

import com.hafizbahtiar.murmur.features.users.dto.*;

import java.util.List;

public interface UserService {
    UserResponse register(UserRegistrationRequest request);

    UserResponse getById(Long id);

    UserResponse getUserByUsername(String username);

    List<UserResponse> getUsers();

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    boolean emailExists(String email);

    boolean usernameExists(String username);
}
