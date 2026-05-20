package com.hafizbahtiar.murmur.features.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID uuid;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String bio;
    private String location;
    private String website;
    private String avatarUrl;
    private String role;
    private Boolean emailVerified;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private UUID uuid;
        private String email;
        private String username;
        private String fullName;
        private String role;
        private Boolean active;
        private Boolean emailVerified;
    }
}
