package com.blog01.backend.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank
    @Size(max = 100, message = "The maximum characters in the title are 100 !")
    private String title;
    @NotBlank
    private String content;

}
