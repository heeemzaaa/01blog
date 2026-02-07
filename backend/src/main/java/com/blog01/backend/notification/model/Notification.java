package com.blog01.backend.notification.model;


import com.blog01.backend.auth.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User actor;

    public enum NotificationType {
        LIKE,           
        COMMENT,
        NEW_POST,       
        REPORT_CREATED,
        REPORT_UPDATE,
        FOLLOW
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private UUID relatedEntityId;

    @Builder.Default
    private boolean seen = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}