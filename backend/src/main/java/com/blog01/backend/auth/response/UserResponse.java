package com.blog01.backend.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import com.blog01.backend.auth.model.User.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String about;
    private Role role;
    private String profileImage;
}