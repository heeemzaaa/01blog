package com.blog01.backend.post.service;

import org.springframework.stereotype.Service;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.dto.PostRequest;
import com.blog01.backend.post.model.Post;
import com.blog01.backend.post.repository.*;
import com.blog01.backend.post.response.PostResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public ResponseData<List<PostResponse>> getPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        List<PostResponse> response = posts.stream()
            .map(this::mapToPostResponse)
            .collect(Collectors.toList());
        
        return ResponseData.success("Posts fetched successfully !", response);
    }

    public ResponseData<PostResponse> createPost(String email, PostRequest postToCreate) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("There is no user with this email !"));

        Post post = Post.builder()
            .user(user)
            .title(postToCreate.getTitle())
            .content(postToCreate.getContent())
            .media(postToCreate.getMedia())
            .build();

        Post saved = postRepository.save(post);

        return ResponseData.success("Post created successfully !", mapToPostResponse(saved));
    }

    public ResponseData<PostResponse> updatePost(String email, UUID id, PostRequest postToUpdate) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("There is no user with this email !"));

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find this post !"));

        if (!post.getUser().getId().equals(user.getId())) {
            return ResponseData.error("You can't modify this post because it is not yours !");
        }

        post.setTitle(postToUpdate.getTitle());
        post.setContent(postToUpdate.getContent());
        if (postToUpdate.getMedia() != null) {
            post.setMedia(postToUpdate.getMedia());
        }

        Post saved = postRepository.save(post);

        return ResponseData.success("Post updated successfully !", mapToPostResponse(saved));
    }

    public ResponseData<String> deletePost(String email, UUID postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getId().equals(user.getId())) {
            return ResponseData.error("You are not authorized to delete this post");
        }

        postRepository.delete(post);
        
        return ResponseData.success("Post deleted successfully", null);
    }

    private PostResponse mapToPostResponse(Post p) {
        long likesCount = likeRepository.countByPost(p);
        long commentsCount = commentRepository.countByPost(p);
        return PostResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .media(p.getMedia())
                .createdAt(p.getCreatedAt())
                .likesCount(likesCount)
                .commentsCount(commentsCount)
                .user(UserResponse.builder()
                        .id(p.getUser().getId())
                        .firstName(p.getUser().getFirstName())
                        .lastName(p.getUser().getLastName())
                        .username(p.getUser().getUsername())
                        .profileImage(p.getUser().getProfileImage())
                        .build())
                .build();
    }
}