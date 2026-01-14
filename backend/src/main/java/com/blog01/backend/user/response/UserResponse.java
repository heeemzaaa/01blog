package com.blog01.backend.user.response;

import com.blog01.backend.user.model.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

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
    private Role role;
    private String profileImage;
}