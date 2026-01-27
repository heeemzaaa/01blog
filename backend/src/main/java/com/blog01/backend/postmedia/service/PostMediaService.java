package com.blog01.backend.postmedia.service;

import com.blog01.backend.post.model.Post;
import com.blog01.backend.postmedia.response.PostMediaResponse;
import com.blog01.backend.postmedia.model.PostMedia;
import com.blog01.backend.postmedia.repository.PostMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostMediaService {

    private static final int MAX_MEDIA_COUNT = 5;
    private static final long MAX_MEDIA_SIZE = 10 * 1024 * 1024;
    private static final Path UPLOAD_ROOT = Paths.get(System.getProperty("user.dir"), "uploads");

    private final PostMediaRepository postMediaRepository;

    public void handlePostMedias(Post post, List<MultipartFile> medias) {

        if (medias == null || medias.isEmpty()) {
            return;
        }

        long existingCount = postMediaRepository.countByPost(post);
        if (existingCount + medias.size() > MAX_MEDIA_COUNT) {
            throw new IllegalArgumentException("A post can contain at most 5 medias");
        }

        for (MultipartFile media : medias) {

            if (media.isEmpty()) {
                throw new IllegalArgumentException("Media file is empty");
            }

            if (media.getSize() > MAX_MEDIA_SIZE) {
                throw new IllegalArgumentException("Media size must not be more than 10MB");
            }

            String contentType = media.getContentType();
            if (contentType == null ||
                    !(contentType.startsWith("image/") || contentType.startsWith("video/"))) {
                throw new IllegalArgumentException("Only image and video files are allowed");
            }

            String originalName = media.getOriginalFilename();
            String extension = (originalName != null && originalName.contains("."))
                    ? originalName.substring(originalName.lastIndexOf('.') + 1)
                    : "bin";

            String filename = UUID.randomUUID() + "." + extension;

            Path fullPath = UPLOAD_ROOT
                    .resolve("posts")
                    .resolve(post.getId().toString())
                    .resolve(filename);

            try {
                // create uploads/posts/{postId}
                Files.createDirectories(fullPath.getParent());

                // 🔥 WRITE FILE (RELIABLE)
                Files.copy(
                        media.getInputStream(),
                        fullPath,
                        StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(
                        "Failed to store media file at path: " + fullPath.toAbsolutePath(),
                        e);
            }

            String relativePath = "uploads/posts/"
                    + post.getId()
                    + "/"
                    + filename;

            PostMedia mediaEntity = PostMedia.builder()
                    .post(post)
                    .type(resolveMediaType(contentType))
                    .filePath(relativePath)
                    .fileSize(media.getSize())
                    .build();

            postMediaRepository.save(mediaEntity);
        }
    }

    public List<PostMediaResponse> buildResponses(Post post) {
        return postMediaRepository.findAllByPost(post)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteAllByPost(Post post) {
        postMediaRepository.deleteAllByPost(post);
    }

    private PostMedia.MediaType resolveMediaType(String contentType) {
        return contentType.startsWith("image/")
                ? PostMedia.MediaType.IMAGE
                : PostMedia.MediaType.VIDEO;
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
