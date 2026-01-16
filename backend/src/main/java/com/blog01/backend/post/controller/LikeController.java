package com.blog01.backend.post.controller;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.post.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<ResponseData<String>> toggleLike(@PathVariable UUID postId, Principal principal) {
        return ResponseEntity.ok(likeService.toggleLike(principal.getName(), postId));
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<ResponseData<List<UserResponse>>> getPostLikes(@PathVariable UUID postId) {
        return ResponseEntity.ok(likeService.getPostLikes(postId));
    }
}