package com.blog01.backend.medias.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

import com.blog01.backend.medias.model.PostMedias.MediaType;

@Getter
@Builder
@AllArgsConstructor
public class PostMediaResponse {
    private UUID id;
    private MediaType type;
    private String url;
}
