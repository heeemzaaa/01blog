package com.blog01.backend.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    @Size(min = 3, message = "Comment must contain at least 3 characters")
    private String content;
}
