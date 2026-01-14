package com.blog01.backend.auth.dto;

import lombok.Data;

@Data
public class UserLogin {
    private String email;
    private String password;
}
