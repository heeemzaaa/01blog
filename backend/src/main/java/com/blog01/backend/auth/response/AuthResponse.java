package com.blog01.backend.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String about;
    private String role;
    private String profileImage;
}