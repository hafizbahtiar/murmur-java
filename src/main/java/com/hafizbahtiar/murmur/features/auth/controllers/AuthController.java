package com.hafizbahtiar.murmur.features.auth.controllers;

import com.hafizbahtiar.murmur.common.dto.ApiResponse;
import com.hafizbahtiar.murmur.common.util.ResponseUtil;
import com.hafizbahtiar.murmur.features.auth.dto.SignInRequest;
import com.hafizbahtiar.murmur.features.auth.dto.SignInResponse;
import com.hafizbahtiar.murmur.features.auth.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        SignInResponse response = authService.signIn(request.getEmail(), request.getPassword());
        return ResponseUtil.ok(response, "Sign in successful");
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<Void>> signOut(Authentication authentication) {
        authService.signOut(authentication.getName());
        return ResponseUtil.ok(null, "Sign out successful");
    }
}
