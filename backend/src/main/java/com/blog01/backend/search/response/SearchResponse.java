package com.blog01.backend.search.response;

import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.post.response.PostResponse;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.domain.Page;


@Data
@Builder
public class SearchResponse {
    private Page<UserResponse> users;
    private Page<PostResponse> posts;
}
