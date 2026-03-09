package com.blog01.backend.profile.controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.profile.dto.EditProfileRequest;
import com.blog01.backend.profile.response.ProfileResponse;
import com.blog01.backend.profile.service.ProfileService;

import jakarta.validation.Valid;
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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<ProfileResponse>> editProfile(
            @PathVariable UUID id,
            @Valid @RequestPart("data") EditProfileRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            Principal principal) {

        ProfileResponse response = profileService.editProfile(
                principal.getName(),
                id,
                request,
                profileImage);

        return ResponseEntity.ok(
                ResponseData.success("Profile updated successfully", response));
    }
}
