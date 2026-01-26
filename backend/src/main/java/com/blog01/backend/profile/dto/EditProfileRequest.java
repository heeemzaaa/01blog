package com.blog01.backend.profile.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String about;
}
