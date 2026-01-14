package com.blog01.backend.subscribes.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.blog01.backend.user.model.User;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscribes")
@EqualsAndHashCode(of = "id")
@Builder
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User subscriber;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime sucscribedAt;
}
