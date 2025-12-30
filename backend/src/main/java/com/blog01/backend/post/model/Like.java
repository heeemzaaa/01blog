package com.blog01.backend.post.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.blog01.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Post post;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime likedAt;
}
