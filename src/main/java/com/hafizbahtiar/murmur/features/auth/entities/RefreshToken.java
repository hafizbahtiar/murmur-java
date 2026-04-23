package com.hafizbahtiar.murmur.features.auth.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Long userId;

    private String deviceInfo;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    private boolean revoke = false;
    private String replacedByToken;

}
