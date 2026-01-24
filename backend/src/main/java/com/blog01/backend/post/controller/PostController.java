package com.blog01.backend.post.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.dto.PostRequest;
import com.blog01.backend.post.response.PostResponse;
import com.blog01.backend.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(
            value = "/create",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ResponseData<PostResponse>> createPost(
            @RequestPart("post") PostRequest request,
            @RequestPart(value = "medias", required = false) List<MultipartFile> medias,
            Principal principal
    ) {
        return ResponseEntity.ok(
                postService.createPost(principal.getName(), request, medias)
        );
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ResponseData<PostResponse>> updatePost(
            @PathVariable UUID id,
            @RequestPart("post") PostRequest request,
            @RequestPart(value = "medias", required = false) List<MultipartFile> medias,
            Principal principal
    ) {
        return ResponseEntity.ok(
                postService.updatePost(principal.getName(), id, request, medias)
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<String>> deletePost(
            @PathVariable UUID id,
            Principal principal
    ) {
        return ResponseEntity.ok(
                postService.deletePost(principal.getName(), id)
        );
    }
}
