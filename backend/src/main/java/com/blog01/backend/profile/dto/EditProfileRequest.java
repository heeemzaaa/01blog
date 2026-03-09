package com.blog01.backend.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    @NotBlank
    @Size(min = 3, max = 24, message = "Invalid first Name !")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 24, message = "Invalid last Name !")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 24, message = "Invalid username !")
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank
    @Size(min = 20, max = 200, message = "The about me part should contain between 20 and 200 character !")
    private String about;
}
