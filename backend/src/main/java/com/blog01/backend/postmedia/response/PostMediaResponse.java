package com.blog01.backend.postmedia.response;

import com.blog01.backend.postmedia.model.PostMedia.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PostMediaResponse {
    private UUID id;
    private MediaType type;
    private String url;
}
