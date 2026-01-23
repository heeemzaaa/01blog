package com.blog01.backend.post.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.dto.PostRequest;
import com.blog01.backend.post.response.PostResponse;
import com.blog01.backend.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.security.Principal;
import java.util.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<ResponseData<List<PostResponse>>> getPosts(Principal principal) {
      
        return ResponseEntity.ok(postService.getPosts(principal.getName()));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<PostResponse>> createPost(@RequestBody PostRequest request,
            Principal principal) {
        return ResponseEntity.ok(postService.createPost(principal.getName(), request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData<PostResponse>> updatePost(@PathVariable UUID id,
            @RequestBody PostRequest request, Principal principal) {
        return ResponseEntity.ok(postService.updatePost(principal.getName(), id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<String>> deletePost(@PathVariable UUID id, Principal principal) {
        return ResponseEntity.ok(postService.deletePost(principal.getName(), id));
    }

}
