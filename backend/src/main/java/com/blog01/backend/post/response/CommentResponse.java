package com.blog01.backend.post.response;

import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.post.model.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private UUID id;
    private UserResponse user;
    private String content;
    private LocalDateTime commentedAt;
    private boolean isMyComment;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .user(
                    UserResponse.builder()
                        .id(comment.getUser().getId())
                        .username(comment.getUser().getUsername())
                        .firstName(comment.getUser().getFirstName())
                        .lastName(comment.getUser().getLastName())
                        .profileImage(comment.getUser().getProfileImage())
                        .build()
                )
                .content(comment.getContent())
                .commentedAt(comment.getCommentedAt())
                .isMyComment(false) // set later in service
                .build();
    }
}
