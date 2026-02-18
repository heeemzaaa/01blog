package com.blog01.backend.post.response;

import java.time.LocalDateTime;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.medias.response.PostMediaResponse;
import com.blog01.backend.post.model.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
        private UUID id;
        private UserResponse user;
        private String title;
        private String content;
        private List<PostMediaResponse> medias;
        private LocalDateTime createdAt;
        private boolean isLiked;
        private boolean visible;
        private boolean reviewed;
        private long likesCount;
        private long commentsCount;
        private boolean isMyPost;

        public PostResponse(Post post) {
                this.id = post.getId();
                this.title = post.getTitle();
                this.user = new UserResponse(post.getUser());
                this.createdAt = post.getCreatedAt();
        }

        public static PostResponse fromEntity(Post post) {
                return PostResponse.builder()
                                .id(post.getId())
                                .user(UserResponse.builder()
                                                .id(post.getUser().getId())
                                                .username(post.getUser().getUsername())
                                                .firstName(post.getUser().getFirstName())
                                                .lastName(post.getUser().getLastName())
                                                .profileImage(post.getUser().getProfileImage())
                                                .build())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .medias(
                                                post.getMedias() == null
                                                                ? List.of()
                                                                : post.getMedias().stream()
                                                                                .map(PostMediaResponse::fromEntity)
                                                                                .toList())
                                .createdAt(post.getCreatedAt())
                                .visible(post.isVisible())
                                .reviewed(post.isReviewed())
                                .likesCount(0)
                                .commentsCount(0)
                                .isLiked(false)
                                .isMyPost(false)
                                .build();
        }

}
