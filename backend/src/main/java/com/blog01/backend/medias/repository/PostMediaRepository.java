package com.blog01.backend.medias.repository;

import com.blog01.backend.medias.model.PostMedias;
import com.blog01.backend.post.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostMediaRepository extends JpaRepository<PostMedias, UUID> {

    List<PostMedias> findAllByPost(Post post);

    long countByPost(Post post);

    void deleteAllByPost(Post post);

    void deleteAllByIdIn(List<UUID> ids);

    List<PostMedias> findAllByIdIn(List<UUID> ids);

}
