package com.blog01.backend.post.service;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.model.Notification.NotificationType;
import com.blog01.backend.notification.service.NotificationService;
import com.blog01.backend.post.model.Like;
import com.blog01.backend.post.model.Post;
import com.blog01.backend.post.repository.LikeRepository;
import com.blog01.backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ResponseData<String> toggleLike(String email, UUID postId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isActive()) {
            throw new BadCredentialsException("You are banned !");
        }
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.isVisible()) {
            throw new AccessDeniedException("You can't like or dislike an invisible post !");
        }

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            // If exists -> UNLIKE
            likeRepository.delete(existingLike.get());
            return ResponseData.success("Post unliked successfully", null);
        } else {
            // If not exists -> LIKE
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(like);

            notificationService.sendNotification(
                    post.getUser(),
                    user,
                    NotificationType.LIKE,
                    post.getId());
            return ResponseData.success("Post liked successfully", null);
        }
    }

    // 2. Get Users who liked a post
    public ResponseData<List<UserResponse>> getPostLikes(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        List<Like> likes = likeRepository.findByPost(post);

        List<UserResponse> likers = likes.stream()
                .map(like -> mapToUserResponse(like.getUser()))
                .collect(Collectors.toList());

        return ResponseData.success("Likes fetched successfully", likers);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .build();
    }
}