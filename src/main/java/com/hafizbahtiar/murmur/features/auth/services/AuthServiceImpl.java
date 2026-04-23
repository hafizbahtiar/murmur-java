package com.hafizbahtiar.murmur.features.auth.services;

import com.hafizbahtiar.murmur.features.users.dto.UserResponse;

import java.util.UUID;

public class AuthServiceImpl implements AuthService {
    @Override
    public UserResponse signIn(String email, String password) {
        return null;
    }

    @Override
    public boolean signOut(UUID uuid) {
        return false;
    }
}
