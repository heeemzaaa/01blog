package com.blog01.backend.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog01.backend.auth.dto.UserLogin;
import com.blog01.backend.auth.dto.UserRegister;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.auth.service.AuthService;
import com.blog01.backend.common.response.ResponseData;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService us;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<UserResponse>> login(@RequestBody UserLogin request) {
        ResponseData<UserResponse> response = us.login(request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register", consumes = "multipart/form-data")
public ResponseEntity<ResponseData<UserResponse>> register(
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam(required = false) String about,
        @RequestParam(required = false) MultipartFile profileImage
) {

    UserRegister request = new UserRegister();
    request.setFirstName(firstName);
    request.setLastName(lastName);
    request.setUsername(username);
    request.setEmail(email);
    request.setPassword(password);
    request.setAbout(about);

    ResponseData<UserResponse> response = us.register(request, profileImage);

    if (!response.isSuccess()) {
        return ResponseEntity.status(409).body(response);
    }

    return ResponseEntity.status(201).body(response);
}


    @GetMapping("/me")
    public ResponseEntity<ResponseData<UserResponse>> getMe(Principal principal) {
        ResponseData<UserResponse> response = us.getMe(principal.getName());
        return ResponseEntity.ok(response);
    }

}
