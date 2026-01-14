package com.blog01.backend.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.user.dto.UserLogin;
import com.blog01.backend.user.dto.UserRegister;
import com.blog01.backend.user.response.AuthResponse;
import com.blog01.backend.user.response.UserResponse;
import com.blog01.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService us;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<AuthResponse>> login(@RequestBody UserLogin request) {
        ResponseData<AuthResponse> response = us.login(request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<UserResponse>> register(@RequestBody UserRegister request) {
        ResponseData<UserResponse> response = us.register(request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(409).body(response);
        }

        return ResponseEntity.status(201).body(response);
    }

}
