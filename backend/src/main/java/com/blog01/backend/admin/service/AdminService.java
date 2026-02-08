package com.blog01.backend.admin.service;

import com.blog01.backend.admin.response.AdminDashboardResponse;
import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.model.Post;
import com.blog01.backend.post.model.Comment;
import com.blog01.backend.post.repository.PostRepository;
import com.blog01.backend.post.repository.CommentRepository;
import com.blog01.backend.post.response.CommentResponse;
import com.blog01.backend.post.response.PostResponse;
import com.blog01.backend.report.model.Report;
import com.blog01.backend.report.model.Report.StatusOfReports;
import com.blog01.backend.report.model.Report.Target;
import com.blog01.backend.report.repository.ReportRepository;
import com.blog01.backend.report.response.ReportResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    // ======================== USERS ========================

    public ResponseData<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseData.success("All users fetched successfully", userResponses);
    }

    public ResponseData<String> banUser(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseData.error("User not found");
        }

        User user = optionalUser.get();
        user.setActive(false);
        user.setBannedAt(LocalDateTime.now());

        userRepository.save(user);
        return ResponseData.success("User banned successfully", null);
    }

    public ResponseData<String> unbanUser(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseData.error("User not found");
        }

        User user = optionalUser.get();
        user.setActive(true);
        user.setBannedAt(null);

        userRepository.save(user);
        return ResponseData.success("User unbanned successfully", null);
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
        List<Post> posts = postRepository.findAll();

        List<PostResponse> responses = posts.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseData.success("All posts fetched successfully", responses);
    }

    public ResponseData<String> hidePost(UUID postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return ResponseData.error("Post not found");
        }

        Post post = optionalPost.get();
        post.setVisible(false);
        post.setReviewed(true);

        postRepository.save(post);
        return ResponseData.success("Post hidden successfully", null);
    }

    public ResponseData<String> restorePost(UUID postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return ResponseData.error("Post not found");
        }

        Post post = optionalPost.get();
        post.setVisible(true);

        postRepository.save(post);
        return ResponseData.success("Post restored successfully", null);
    }

    public ResponseData<String> deletePost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            return ResponseData.error("Post not found");
        }

        postRepository.deleteById(postId);
        return ResponseData.success("Post deleted successfully", null);
    }

    // ======================== COMMENTS ========================

    public ResponseData<List<CommentResponse>> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        List<CommentResponse> responses = comments.stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseData.success("All comments fetched successfully", responses);
    }

    public ResponseData<String> hideComment(UUID commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            return ResponseData.error("Comment not found");
        }

        Comment comment = optionalComment.get();
        comment.setVisible(false);
        comment.setReviewed(true);

        commentRepository.save(comment);
        return ResponseData.success("Comment hidden successfully", null);
    }

    public ResponseData<String> deleteComment(UUID commentId) {
        if (!commentRepository.existsById(commentId)) {
            return ResponseData.error("Comment not found");
        }

        commentRepository.deleteById(commentId);
        return ResponseData.success("Comment deleted successfully", null);
    }

    // ======================== REPORTS ========================

    public ResponseData<List<ReportResponse>> getAllReports() {
        List<Report> reports = reportRepository.findAll();

        List<ReportResponse> responses = reports.stream()
                .map(ReportResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseData.success("All reports fetched successfully", responses);
    }

    public ResponseData<String> reviewReport(UUID id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Report not found"));

        if (report.getStatus() != StatusOfReports.PENDING) {
            throw new IllegalArgumentException("Report is already reviewed");
        }

        report.setStatus(StatusOfReports.REVIEWED);
        reportRepository.save(report);

        return ResponseData.success("Report reviewed successfully", null);
    }

    public ResponseData<String> resolveReport(UUID id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Report not found"));

        if (report.getStatus() == StatusOfReports.ACTIONED) {
            throw new IllegalArgumentException("Report is already resolved");
        }

        report.setStatus(StatusOfReports.ACTIONED);
        reportRepository.save(report);

        return ResponseData.success("Report actioned successfully", null);
    }

    // ======================== DASHBOARD ========================

    public ResponseData<AdminDashboardResponse> getDashboardStats() {

        long totalUsers = userRepository.count();
        long totalPosts = postRepository.count();
        long totalComments = commentRepository.count();
        long totalReports = reportRepository.count();

        Map<String, Long> reportsByType = new HashMap<>();
        reportsByType.put("POST", reportRepository.countByTargetType(Target.POST));
        reportsByType.put("COMMENT", reportRepository.countByTargetType(Target.COMMENT));
        reportsByType.put("USER", reportRepository.countByTargetType(Target.USER));

        AdminDashboardResponse response = AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalPosts(totalPosts)
                .totalComments(totalComments)
                .totalReports(totalReports)
                .reportsByType(reportsByType)
                .build();

        return ResponseData.success("Dashboard stats fetched successfully", response);
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
