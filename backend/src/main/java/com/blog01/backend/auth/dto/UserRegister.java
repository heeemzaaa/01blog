package com.blog01.backend.auth.dto;

import lombok.Data;


@Data
public class UserRegister {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String profileImage;
}
