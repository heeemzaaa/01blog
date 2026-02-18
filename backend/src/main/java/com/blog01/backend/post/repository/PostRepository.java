package com.blog01.backend.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.blog01.backend.auth.model.User;
import com.blog01.backend.post.model.Post;
import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByUserInOrderByCreatedAtDesc(List<User> users);

    List<Post> findByUserOrderByCreatedAtDesc(User user);

    @Query("""
                SELECT p FROM Post p
                WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Post> searchByTitle(String keyword, Pageable pageable);

    long countByUser(User user);
}