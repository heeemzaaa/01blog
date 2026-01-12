package com.blog01.backend.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.user.dto.UserLogin;
import com.blog01.backend.user.dto.UserRegister;
import com.blog01.backend.user.model.User;
import com.blog01.backend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService us;

    public UserController(UserService us) {
        this.us = us;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData<User>> login(@RequestBody UserLogin request) {
        ResponseData<User> response = us.login(request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<User>> register(@RequestBody UserRegister request) {
        ResponseData<User> response = us.register(request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(409).body(response);
        }

        return ResponseEntity.status(201).body(response);
    }

}
