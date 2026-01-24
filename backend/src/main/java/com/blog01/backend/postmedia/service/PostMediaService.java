package com.blog01.backend.postmedia.service;

import com.blog01.backend.post.model.Post;
import com.blog01.backend.postmedia.response.PostMediaResponse;
import com.blog01.backend.postmedia.model.PostMedia;
import com.blog01.backend.postmedia.repository.PostMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostMediaService {

    private static final int MAX_MEDIA_COUNT = 5;
    private static final long MAX_MEDIA_SIZE = 10 * 1024 * 1024;

    private final PostMediaRepository postMediaRepository;

    /**
     * Called from PostService when creating or updating a post
     */
    public void handlePostMedias(Post post, List<MultipartFile> medias) {

        if (medias == null || medias.isEmpty()) {
            return;
        }

        long existingCount = postMediaRepository.countByPost(post);
        if (existingCount + medias.size() > MAX_MEDIA_COUNT) {
            throw new RuntimeException("A post can contain at most 5 medias");
        }

        for (MultipartFile media : medias) {

            if (media.isEmpty()) {
                throw new RuntimeException("Media file is empty");
            }

            if (media.getSize() > MAX_MEDIA_SIZE) {
                throw new RuntimeException("Media size must not be more than 10MB");
            }

            String contentType = media.getContentType();
            if (contentType == null ||
                    !(contentType.startsWith("image/") || contentType.startsWith("video/"))) {
                throw new RuntimeException("Only image and video files are allowed");
            }

            PostMedia mediaEntity = PostMedia.builder()
                    .post(post)
                    .type(resolveMediaType(contentType))
                    .filePath(buildStoragePath(post.getId(), media))
                    .fileSize(media.getSize())
                    .build();

            postMediaRepository.save(mediaEntity);
        }
    }

    /**
     * Used when building PostResponse
     */
    public List<PostMediaResponse> buildResponses(Post post) {
        return postMediaRepository.findAllByPost(post)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteAllByPost(Post post) {
        postMediaRepository.deleteAllByPost(post);
    }

    /* ================= Helpers ================= */

    private PostMedia.MediaType resolveMediaType(String contentType) {
        return contentType.startsWith("image/")
                ? PostMedia.MediaType.IMAGE
                : PostMedia.MediaType.VIDEO;
    }

    private String buildStoragePath(UUID postId, MultipartFile media) {

        String originalName = media.getOriginalFilename();
        String extension = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf('.') + 1)
                : "bin";

        return "uploads/posts/" + postId + "/" + UUID.randomUUID() + "." + extension;
    }

    private PostMediaResponse toResponse(PostMedia media) {
        return PostMediaResponse.builder()
                .id(media.getId())
                .type(media.getType())
                .url(buildPublicUrl(media))
                .build();
    }

    private String buildPublicUrl(PostMedia media) {
        return "/media/posts/"
                + media.getPost().getId()
                + "/"
                + media.getFilePath().substring(media.getFilePath().lastIndexOf('/') + 1);
    }
}
