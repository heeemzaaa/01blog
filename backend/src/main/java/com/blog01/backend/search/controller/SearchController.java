package com.blog01.backend.search.controller;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.search.response.SearchResponse;
import com.blog01.backend.search.service.SearchService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseData<SearchResponse> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        // Optional backend protection (recommended)
        if (size > 20) {
            size = 20; // prevent abuse
        }

        return searchService.search(keyword, page, size);
    }
}
