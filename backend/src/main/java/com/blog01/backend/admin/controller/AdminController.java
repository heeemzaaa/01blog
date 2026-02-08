package com.blog01.backend.admin.controller;

import com.blog01.backend.admin.service.AdminService;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.response.PostResponse;
import com.blog01.backend.post.response.CommentResponse;
import com.blog01.backend.report.response.ReportResponse;
import com.blog01.backend.admin.response.AdminDashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ========================= USERS =========================
    @GetMapping("/users")
    public ResponseEntity<ResponseData<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/ban")
    public ResponseEntity<ResponseData<String>> banUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.banUser(id));
    }

    @PutMapping("/users/{id}/unban")
    public ResponseEntity<ResponseData<String>> unbanUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.unbanUser(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ResponseData<String>> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }

    // ========================= POSTS =========================
    @GetMapping("/posts")
    public ResponseEntity<ResponseData<List<PostResponse>>> getAllPosts() {
        return ResponseEntity.ok(adminService.getAllPosts());
    }

    @PutMapping("/posts/{id}/hide")
    public ResponseEntity<ResponseData<String>> hidePost(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.hidePost(id));
    }

    @PutMapping("/posts/{id}/restore")
    public ResponseEntity<ResponseData<String>> restorePost(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.restorePost(id));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ResponseData<String>> deletePost(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.deletePost(id));
    }

    // ========================= COMMENTS =========================
    @GetMapping("/comments")
    public ResponseEntity<ResponseData<List<CommentResponse>>> getAllComments() {
        return ResponseEntity.ok(adminService.getAllComments());
    }

    @PutMapping("/comments/{id}/hide")
    public ResponseEntity<ResponseData<String>> hideComment(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.hideComment(id));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ResponseData<String>> deleteComment(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.deleteComment(id));
    }

    // ========================= REPORTS =========================
    @GetMapping("/reports")
    public ResponseEntity<ResponseData<List<ReportResponse>>> getAllReports() {
        return ResponseEntity.ok(adminService.getAllReports());
    }

    @PutMapping("/reports/{id}/review")
    public ResponseEntity<ResponseData<String>> reviewReport(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.reviewReport(id));
    }

    @PutMapping("/reports/{id}/resolve")
    public ResponseEntity<ResponseData<String>> resolveReport(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.resolveReport(id));
    }

    // ========================= DASHBOARD =========================
    @GetMapping("/dashboard")
    public ResponseEntity<ResponseData<AdminDashboardResponse>> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }
}
