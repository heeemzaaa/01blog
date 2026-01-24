package com.blog01.backend.admin.service;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // 1. Get All Users (Admin Only)
    public ResponseData<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        
        // Convert Users to safe UserResponse DTOs (hide passwords)
        List<UserResponse> userResponses = users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseData.success("All users fetched successfully", userResponses);
    }

    // 2. Delete a User (Admin Only)
    public ResponseData<String> deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseData.error("User not found");
        }
        userRepository.deleteById(userId);
        return ResponseData.success("User deleted successfully", null);
    }

    // Helper method to map User -> UserResponse
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .build();
    }
}