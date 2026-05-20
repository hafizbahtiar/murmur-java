package com.hafizbahtiar.murmur.features.auth.dto;

import com.hafizbahtiar.murmur.features.users.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String token;

    @Builder.Default
    private String type = "Bearer";

    private Instant expiresAt;
    private String refreshToken;
    private Instant refreshTokenExpiresAt;
    private UserResponse user;
}
