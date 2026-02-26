package com.blog01.backend.auth.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

import jakarta.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword()));

        User user = ur.findByEmail(requestUser.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new BadCredentialsException("You are banned !");
        }

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
                .active(user.isActive())
                .build();

        return ResponseData.success("User logged in successfully !", userResponse);

    }

    public ResponseData<UserResponse> register(
            UserRegister userRequest,
            MultipartFile profileImage) {

        if (ur.existsByEmail(userRequest.getEmail())) {
            throw new DataIntegrityViolationException("Email already existed, try another one !");
        }

        // 1️⃣ Create user without image
        User userToCreate = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .about(userRequest.getAbout())
                .password(pe.encode(userRequest.getPassword()))
                .build();

        User saved = ur.save(userToCreate);

        // 2️⃣ Save image using generated ID
        String imagePath = saveProfileImage(saved.getId(), profileImage);

        if (imagePath != null) {
            saved.setProfileImage(imagePath);
            ur.save(saved);
        }

        // 3️⃣ Continue normally
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
                .active(saved.isActive())
                .build();

        return ResponseData.success("User registered successfully !", userResponse);
    }

    public ResponseData<UserResponse> getMe(String email) {
        User user = ur.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isActive()) {
            throw new BadCredentialsException("You are banned from the application !");
        }
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
                .active(user.isActive())
                .build();

        return ResponseData.success("User validated", me);
    }

    private String saveProfileImage(UUID userId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String uploadDir = "uploads/users/" + userId;
            Path uploadPath = Paths.get(uploadDir);

            Files.createDirectories(uploadPath);

            String extension = Optional.ofNullable(file.getOriginalFilename())
                    .filter(name -> name.contains("."))
                    .map(name -> name.substring(name.lastIndexOf(".")))
                    .orElse(".jpg");

            String fileName = "profile" + extension; // always same name

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            return "/media/users/" + userId + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store profile image", e);
        }
    }

}