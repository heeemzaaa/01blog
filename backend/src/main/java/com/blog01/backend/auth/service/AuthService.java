package com.blog01.backend.auth.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blog01.backend.auth.dto.*;
import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
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

    public ResponseData<UserResponse> login(UserLogin requestUser) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword()));

            User user = ur.findByEmail(requestUser.getEmail()).orElseThrow();

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String jwtToken = jwtUtils.generateToken(userDetails, user.getId());

            long nbr_of_posts = postRepository.countByUser(user);
            long nbr_of_following = subscribesRepository.countBySubscriber(user);
            long nbr_of_followers = subscribesRepository.countByUser(user);
            long nbr_of_notifications = notificationRepository.countByRecipientAndSeenFalse(user);

            UserResponse userResponse = UserResponse.builder()
                    .token(jwtToken)
                    .id(user.getId())
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .profileImage(user.getProfileImage())
                    .nbr_of_posts(nbr_of_posts)
                    .nbr_of_followers(nbr_of_followers)
                    .nbr_of_following(nbr_of_following)
                    .nbr_of_notifications(nbr_of_notifications)
                    .role(user.getRole())
                    .build();

            return ResponseData.success("User logged in successfully !", userResponse);
        } catch (AuthenticationException e) {
            return ResponseData.error("Invalid credentials !");
        }
    }

    public ResponseData<UserResponse> register(
            UserRegister userRequest,
            MultipartFile profileImage) {
        if (ur.existsByEmail(userRequest.getEmail())) {
            return ResponseData.error("Email already existed, try another one !");
        }

        String imagePath = saveProfileImage(profileImage);

        User userToCreate = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .about(userRequest.getAbout())
                .password(pe.encode(userRequest.getPassword()))
                .profileImage(imagePath)
                .build();

        User saved = ur.save(userToCreate);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        String jwtToken = jwtUtils.generateToken(userDetails, saved.getId());

        long nbr_of_posts = postRepository.countByUser(saved);
        long nbr_of_following = subscribesRepository.countBySubscriber(saved);
        long nbr_of_followers = subscribesRepository.countByUser(saved);
        long nbr_of_notifications = notificationRepository.countByRecipientAndSeenFalse(saved);

        UserResponse userResponse = UserResponse.builder()
                .token(jwtToken)
                .id(saved.getId())
                .username(saved.getUsername())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .profileImage(saved.getProfileImage())
                .nbr_of_posts(nbr_of_posts)
                .nbr_of_followers(nbr_of_followers)
                .nbr_of_following(nbr_of_following)
                .nbr_of_notifications(nbr_of_notifications)
                .role(saved.getRole())
                .build();

        return ResponseData.success("User registered successfully !", userResponse);
    }

    public ResponseData<UserResponse> getMe(String email) {
        User user = ur.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        long nbr_of_posts = postRepository.countByUser(user);
        long nbr_of_following = subscribesRepository.countBySubscriber(user);
        long nbr_of_followers = subscribesRepository.countByUser(user);
        long nbr_of_notifications = notificationRepository.countByRecipientAndSeenFalse(user);

        UserResponse me = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImage(user.getProfileImage())
                .nbr_of_posts(nbr_of_posts)
                .nbr_of_followers(nbr_of_followers)
                .nbr_of_following(nbr_of_following)
                .nbr_of_notifications(nbr_of_notifications)
                .role(user.getRole())
                .build();

        return ResponseData.success("User validated", me);
    }

    private String saveProfileImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String uploadDir = "uploads/users/";
            Files.createDirectories(Paths.get(uploadDir));

            String extension = Optional.ofNullable(file.getOriginalFilename())
                    .filter(name -> name.contains("."))
                    .map(name -> name.substring(name.lastIndexOf(".")))
                    .orElse(".jpg");

            String fileName = UUID.randomUUID() + extension;
            Path filePath = Paths.get(uploadDir + fileName);

            Files.write(filePath, file.getBytes());

            return "/media/users/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store profile image", e);
        }
    }

}