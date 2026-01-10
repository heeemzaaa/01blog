package com.blog01.backend.user.dto;

import lombok.Data;

@Data
public class UserLogin {
    private String email;
    private String password;
}
