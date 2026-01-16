package com.blog01.backend.post.dto;

import lombok.Data;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String media;
}
