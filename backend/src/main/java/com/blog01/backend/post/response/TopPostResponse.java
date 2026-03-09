package com.blog01.backend.post.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TopPostResponse {

    private UUID id;
    private String title;
    private String content;
    private UUID authorId;
    private long likesCount;
    private LocalDateTime createdAt;

}