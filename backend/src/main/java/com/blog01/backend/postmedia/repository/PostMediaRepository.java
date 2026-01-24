package com.blog01.backend.postmedia.repository;

import com.blog01.backend.post.model.Post;
import com.blog01.backend.postmedia.model.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostMediaRepository extends JpaRepository<PostMedia, UUID> {

    List<PostMedia> findAllByPost(Post post);

    long countByPost(Post post);

    void deleteAllByPost(Post post);
}
