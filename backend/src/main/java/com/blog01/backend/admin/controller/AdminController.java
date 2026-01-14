package com.blog01.backend.admin.controller;

import com.blog01.backend.admin.service.AdminService;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;

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

    @GetMapping("/users")
    public ResponseEntity<ResponseData<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<String>> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }
}