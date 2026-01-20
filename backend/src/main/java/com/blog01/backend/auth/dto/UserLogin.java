package com.blog01.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLogin {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 8,max = 32, message = "Invalid password !")
    private String password;
}
