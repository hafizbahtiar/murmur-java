package com.hafizbahtiar.murmur.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hafizbahtiar.murmur.features.users.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String token;

    @Builder.Default
    private String type = "Bearer";

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;

    private String refreshToken;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime refreshTokenExpiresAt;

    private UserResponse user;
}
