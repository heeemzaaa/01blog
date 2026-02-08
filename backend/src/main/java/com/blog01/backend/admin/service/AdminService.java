package com.blog01.backend.admin.service;

import com.blog01.backend.admin.response.AdminDashboardResponse;
import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.response.CommentResponse;
import com.blog01.backend.post.response.PostResponse;
import com.blog01.backend.report.response.ReportResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // ======================== USERS ========================
    public ResponseData<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseData.success("All users fetched successfully", userResponses);
    }

    public ResponseData<String> banUser(UUID userId) {
        return null;
    }

    public ResponseData<String> unbanUser(UUID userId) {
        return null;
    }

    public ResponseData<String> deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseData.error("User not found");
        }
        userRepository.deleteById(userId);
        return ResponseData.success("User deleted successfully", null);
    }

    // ======================== POSTS ========================
    public ResponseData<List<PostResponse>> getAllPosts() {
        return null;
    }

    public ResponseData<String> hidePost(UUID postId) {
        return null;
    }

    public ResponseData<String> restorePost(UUID postId) {
        return null;
    }

    public ResponseData<String> deletePost(UUID postId) {
        return null;
    }

    // ======================== COMMENTS ========================
    public ResponseData<List<CommentResponse>> getAllComments() {
        return null;
    }

    public ResponseData<String> hideComment(UUID commentId) {
        return null;
    }

    public ResponseData<String> deleteComment(UUID commentId) {
        return null;
    }

    // ======================== REPORTS ========================
    public ResponseData<List<ReportResponse>> getAllReports() {
        return null;
    }

    public ResponseData<String> reviewReport(UUID id) {
        return null;
    }

    public ResponseData<String> resolveReport(UUID id) {
        return null;
    }

    // ======================== DASHBOARD ========================
    public ResponseData<AdminDashboardResponse> getDashboardStats() {
        return null;
    }

    // ======================== HELPER ========================
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