package com.blog01.backend.post.repository;

import com.blog01.backend.post.model.Comment;
import com.blog01.backend.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;


@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    long countByPost(Post post);

    List<Comment> findByPost(Post post);

    List<Comment> findByPostOrderByCommentedAtAsc(Post post);
}