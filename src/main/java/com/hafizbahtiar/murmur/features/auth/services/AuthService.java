package com.hafizbahtiar.murmur.features.auth.services;

import com.hafizbahtiar.murmur.features.users.dto.UserResponse;

import java.util.UUID;

public interface AuthService {
    UserResponse signIn(String email, String password);

    boolean signOut(UUID uuid);
}
