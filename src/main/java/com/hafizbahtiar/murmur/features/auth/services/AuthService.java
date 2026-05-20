package com.hafizbahtiar.murmur.features.auth.services;

import com.hafizbahtiar.murmur.features.auth.dto.SignInResponse;

public interface AuthService {
    SignInResponse signIn(String email, String password);

    boolean signOut(String email);
}
