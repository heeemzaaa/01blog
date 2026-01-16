package com.blog01.backend.post.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.blog01.backend.auth.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
