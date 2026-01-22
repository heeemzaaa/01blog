package com.blog01.backend.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.post.model.Post;
import java.util.UUID;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByOrderByCreatedAtDesc();

    long countByUser(User user);
}