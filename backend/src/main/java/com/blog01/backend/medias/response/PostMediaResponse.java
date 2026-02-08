package com.blog01.backend.medias.response;

import com.blog01.backend.medias.model.PostMedias;
import com.blog01.backend.medias.model.PostMedias.MediaType;
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
    private String filePath;

    public static PostMediaResponse fromEntity(PostMedias media) {
        return PostMediaResponse.builder()
                .id(media.getId())
                .type(media.getType())
                .filePath(media.getFilePath())
                .build();
    }
}
