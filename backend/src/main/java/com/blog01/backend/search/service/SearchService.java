package com.blog01.backend.search.service;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.post.model.Post;
import com.blog01.backend.post.repository.PostRepository;
import com.blog01.backend.post.response.PostResponse;
import com.blog01.backend.search.response.SearchResponse;
import com.blog01.backend.common.response.ResponseData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public SearchService(UserRepository userRepository,
            PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ResponseData<SearchResponse> search(String keyword, int page, int size) {

        if (keyword == null || keyword.length() < 2) {
            throw new IllegalArgumentException("Search keyword must be at least 2 characters.");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = userRepository.searchByUsername(keyword, pageable);
        Page<Post> posts = postRepository.searchByTitle(keyword, pageable);

        Page<UserResponse> userResponses = users.map(UserResponse::new);
        Page<PostResponse> postResponses = posts.map(PostResponse::new);

        SearchResponse response = SearchResponse.builder()
                .users(userResponses)
                .posts(postResponses)
                .build();

        return ResponseData.success("result", response);
    }
}
