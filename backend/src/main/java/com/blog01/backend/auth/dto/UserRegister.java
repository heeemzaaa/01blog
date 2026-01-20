package com.blog01.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegister {
    @NotBlank
    @Size(min = 5, max = 20, message = "Invalid first name !")
    private String firstName;
    @NotBlank
    @Size(min = 5, max = 20, message = "Invalid last name !")
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 5, max = 15, message = "Invalid user name !")
    private String username;
    @NotBlank
    @Size(min = 8, max = 32, message = "Invalid password !")
    private String password;
    @NotBlank
    private String profileImage;
}
