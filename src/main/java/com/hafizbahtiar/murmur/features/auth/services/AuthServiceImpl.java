package com.hafizbahtiar.murmur.features.auth.services;

import com.hafizbahtiar.murmur.features.auth.dto.SignInResponse;
import com.hafizbahtiar.murmur.features.auth.entities.RefreshToken;
import com.hafizbahtiar.murmur.features.auth.repositories.RefreshTokenRepository;
import com.hafizbahtiar.murmur.features.users.entities.User;
import com.hafizbahtiar.murmur.features.users.mappers.UserMapper;
import com.hafizbahtiar.murmur.features.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Value("${security.jwt.refresh-expiration-time}")
    private long refreshExpiration;

    @Override
    @Transactional
    public SignInResponse signIn(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!user.isActive()) {
            throw new DisabledException("Account is disabled");
        }

        user.updateLastLogin();
        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole())
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshTokenValue = UUID.randomUUID().toString();

        Instant now = Instant.now();
        Instant accessExpiresAt = now.plusMillis(jwtService.getExpirationTime());
        Instant refreshExpiresAt = now.plusMillis(refreshExpiration);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setUserId(user.getId());
        refreshToken.setIssuedAt(now);
        refreshToken.setExpiresAt(refreshExpiresAt);
        refreshTokenRepository.save(refreshToken);

        return SignInResponse.builder()
                .token(accessToken)
                .expiresAt(accessExpiresAt)
                .refreshToken(refreshTokenValue)
                .refreshTokenExpiresAt(refreshExpiresAt)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public boolean signOut(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(user -> {
                    List<RefreshToken> tokens = refreshTokenRepository.findByUserIdAndRevokedFalse(user.getId());
                    tokens.forEach(t -> t.setRevoked(true));
                    refreshTokenRepository.saveAll(tokens);
                    return true;
                })
                .orElse(false);
    }
}
