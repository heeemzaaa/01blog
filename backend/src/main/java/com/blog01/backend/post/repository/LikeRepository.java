package com.blog01.backend.post.repository;

import com.blog01.backend.post.model.Like;
import com.blog01.backend.post.model.Post;
import com.blog01.backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    boolean existsByUserAndPost(User user, Post post);

    Optional<Like> findByUserAndPost(User user, Post post);

    List<Like> findByPost(Post post);
    
    long countByPost(Post post);
}