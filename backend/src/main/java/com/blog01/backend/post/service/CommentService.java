package com.blog01.backend.post.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.model.Notification.NotificationType;
import com.blog01.backend.notification.service.NotificationService;
import com.blog01.backend.post.dto.CommentRequest;
import com.blog01.backend.post.model.Comment;
import com.blog01.backend.post.model.Post;
import com.blog01.backend.post.repository.CommentRepository;
import com.blog01.backend.post.repository.PostRepository;
import com.blog01.backend.post.response.CommentResponse;
import java.util.stream.Collectors;
import java.util.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    public ResponseData<List<CommentResponse>> getCommentsByPost(UUID postId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found !"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found !"));
        List<Comment> comments = commentRepository.findByPostOrderByCommentedAtAsc(post);
        List<CommentResponse> response = comments.stream()
                .map(c -> mapToCommentResponse(c, user.getId()))
                .collect(Collectors.toList());
        return ResponseData.success("Comments fetched successfully !", response);
    }

    public ResponseData<CommentResponse> createComment(String email, UUID postId, CommentRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found !"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found !"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();

        Comment saved = commentRepository.save(comment);

        notificationService.sendNotification(
                post.getUser(),
                user,
                NotificationType.COMMENT,
                post.getId());

        return ResponseData.success("Comment added successfully !", mapToCommentResponse(saved, user.getId()));
    }

    public ResponseData<CommentResponse> updateComment(String email, UUID commentId, CommentRequest commentToUpdate,
            MultipartFile commentImage) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found !"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found !"));

        if (!user.getId().equals(comment.getUser().getId())) {
            return ResponseData.error("You cannot update a comment that is not yours !");
        }

        comment.setContent(commentToUpdate.getContent());
        Comment saved = commentRepository.save(comment);
        return ResponseData.success("Comment updated successfully !", mapToCommentResponse(saved, user.getId()));
    }

    public ResponseData<String> deleteComment(String email, UUID postId, UUID commentId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found !"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found !"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found !"));

        if (user.getId().equals(post.getUser().getId()) || user.getId().equals(comment.getUser().getId())) {
            commentRepository.delete(comment);
            return ResponseData.success("Comment is deleted successfully !", null);
        }
        return ResponseData.error("You cannot delete this comment !");
    }

    private CommentResponse mapToCommentResponse(Comment comment, UUID currentUserId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .user(UserResponse.builder()
                        .id(comment.getUser().getId())
                        .firstName(comment.getUser().getFirstName())
                        .lastName(comment.getUser().getLastName())
                        .username(comment.getUser().getUsername())
                        .profileImage(comment.getUser().getProfileImage())
                        .build())
                .content(comment.getContent())
                .commentedAt(comment.getCommentedAt())
                .visible(comment.isVisible())
                .reviewed(comment.isReviewed())
                .isMyComment(comment.getUser().getId().equals(currentUserId))
                .build();
    }
}
