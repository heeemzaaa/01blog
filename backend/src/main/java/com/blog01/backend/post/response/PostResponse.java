package com.blog01.backend.post.response;

import java.time.LocalDateTime;
import com.blog01.backend.auth.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private UUID id;
    private UserResponse user;
    private String title;
    private String content;
    private String media;
    private LocalDateTime createdAt;
    private long likesCount;
    private long commentsCount;
}
