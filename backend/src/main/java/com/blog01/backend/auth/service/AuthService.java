package com.blog01.backend.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog01.backend.auth.dto.*;
import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.AuthResponse;
import com.blog01.backend.auth.response.MeResponse;
import com.blog01.backend.common.response.*;
import com.blog01.backend.notification.repository.NotificationRepository;
import com.blog01.backend.post.repository.PostRepository;
import com.blog01.backend.security.CustomUserDetailsService;
import com.blog01.backend.security.JwtUtils;
import com.blog01.backend.subscribes.repository.SubscribesRepository;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository ur;
    private final PasswordEncoder pe;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final SubscribesRepository subscribesRepository;

    public ResponseData<AuthResponse> login(UserLogin requestUser) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword()));

            User user = ur.findByEmail(requestUser.getEmail()).orElseThrow();

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String jwtToken = jwtUtils.generateToken(userDetails, user.getId());

            AuthResponse authResponse = AuthResponse.builder()
                    .token(jwtToken)
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .about(user.getAbout())
                    .role(user.getRole().name())
                    .profileImage(user.getProfileImage())
                    .build();

            return ResponseData.success("User logged in successfully !", authResponse);
        } catch (AuthenticationException e) {
            return ResponseData.error("Invalid credentials !");
        }
    }

    public ResponseData<AuthResponse> register(UserRegister userRequest) {
        if (ur.existsByEmail(userRequest.getEmail())) {
            return ResponseData.error("Email already existed, try another one !");
        }
        User userToCreate = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .about(userRequest.getAbout())
                .password(pe.encode(userRequest.getPassword()))
                .profileImage(userRequest.getProfileImage())
                .build();

        User saved = ur.save(userToCreate);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        String jwtToken = jwtUtils.generateToken(userDetails, saved.getId());

        AuthResponse authResponse = AuthResponse.builder()
                .token(jwtToken)
                .id(saved.getId())
                .email(saved.getEmail())
                .username(saved.getUsername())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .about(saved.getAbout())
                .role(saved.getRole().name())
                .profileImage(saved.getProfileImage())
                .build();

        return ResponseData.success("User registered successfully !", authResponse);
    }


    public ResponseData<MeResponse> getMe(String email) {
        User user = ur.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        long nbr_of_posts = postRepository.countByUser(user);
        long nbr_of_following = subscribesRepository.countBySubscriber(user);
        long nbr_of_followers = subscribesRepository.countByUser(user);
        long nbr_of_notifications = notificationRepository.countByRecipientAndSeenFalse(user);

        MeResponse me = MeResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImage(user.getProfileImage())
                .nbr_of_posts(nbr_of_posts)
                .nbr_of_followers(nbr_of_followers)
                .nbr_of_following(nbr_of_following)
                .nbr_of_notifications(nbr_of_notifications)
                .build();

        return ResponseData.success("User validated", me);
    }
}