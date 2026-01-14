package com.blog01.backend.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.blog01.backend.user.model.User;
import com.blog01.backend.user.dto.*;
import com.blog01.backend.common.response.*;
import com.blog01.backend.security.CustomUserDetailsService;
import com.blog01.backend.security.JwtUtils;
import com.blog01.backend.user.repository.UserRepository;
import com.blog01.backend.user.response.AuthResponse;
import com.blog01.backend.user.response.UserResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository ur;
    private final PasswordEncoder pe;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public ResponseData<AuthResponse> login(UserLogin requestUser) {
        try {
           authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword())     
           );

           User user = ur.findByEmail(requestUser.getEmail()).orElseThrow();

           UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
           String jwtToken = jwtUtils.generateToken(userDetails, user.getId());

           AuthResponse authResponse = AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .profileImage(user.getProfileImage())
                .build();

            return ResponseData.success("User logged in successfully !", authResponse);
        } catch (AuthenticationException e) {
            return ResponseData.error("Invalid credentials !");
        }
    }

    public ResponseData<UserResponse> register(UserRegister userRequest) {
        if (ur.existsByEmail(userRequest.getEmail())) {
            return ResponseData.error("Email already existed, try another one !");
        }
        User userToCreate = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())    
                .email(userRequest.getEmail())
                .password(pe.encode(userRequest.getPassword()))
                .profileImage(userRequest.getProfileImage())
                .build();

        User saved = ur.save(userToCreate);

        UserResponse userResponse = UserResponse.builder()
            .id(saved.getId())
            .firstName(saved.getFirstName())
            .lastName(saved.getLastName())
            .email(saved.getEmail())
            .username(saved.getUsername())
            .role(saved.getRole())
            .profileImage(saved.getProfileImage())
            .build();

        return ResponseData.success("User registered successfully !", userResponse);
    }
}