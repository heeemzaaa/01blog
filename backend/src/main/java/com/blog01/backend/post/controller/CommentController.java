package com.blog01.backend.post.controller;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.dto.CommentRequest;
import com.blog01.backend.post.response.CommentResponse;
import com.blog01.backend.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // POST /api/posts/{postId}/comments
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ResponseData<CommentResponse>> addComment(@PathVariable UUID postId,
            @RequestBody CommentRequest request,
            Principal principal) {
        return ResponseEntity.ok(commentService.createComment(principal.getName(), postId, request));
    }

    // GET /api/posts/{postId}/comments
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ResponseData<List<CommentResponse>>> getComments(@PathVariable UUID postId,
            Principal principal) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, principal.getName()));
    }

    // DELETE /api/comments/{commentId}
    // Note: We don't need postId here, the commentId is unique enough
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseData<String>> deleteComment(@PathVariable UUID commentId,
            @PathVariable UUID postId,
            Principal principal) {
        return ResponseEntity.ok(commentService.deleteComment(principal.getName(), postId, commentId));
    }

    @PutMapping(value = "comments/{commentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<CommentResponse>> updateComment(@PathVariable UUID commentId,
            Principal principal,
            @RequestPart("data") CommentRequest request,
            @RequestPart(value = "commentImage", required = false) MultipartFile commentImage) {

        return ResponseEntity.ok(commentService.updateComment(principal.getName(), commentId, request, commentImage));
    }
}