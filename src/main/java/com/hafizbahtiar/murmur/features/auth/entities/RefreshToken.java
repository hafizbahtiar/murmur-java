package com.hafizbahtiar.murmur.features.auth.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_tokens_token", columnList = "token", unique = true),
        @Index(name = "idx_refresh_tokens_user_id", columnList = "userId"),
        @Index(name = "idx_refresh_tokens_user_revoked", columnList = "userId, revoked")
})
@Data
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Long userId;

    private String deviceInfo;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    private String replacedByToken;
}
