package com.blog01.backend.profile.controller;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.profile.response.ProfileResponse;
import com.blog01.backend.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseData<ProfileResponse> getProfile(
            @PathVariable UUID userId,
            Authentication authentication) {

        return profileService.getProfile(userId, authentication);
    }
}
