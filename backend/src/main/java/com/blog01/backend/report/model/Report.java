package com.blog01.backend.report.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.blog01.backend.auth.model.User;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User reporter;

    public enum Target {
        POST,
        COMMENT,
        USER
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Target targetType;

    @Column(nullable = false)
    private UUID targetId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    public enum StatusOfReports {
        PENDING,
        REVIEWED,
        DISMISSED,
        ACTIONED
    }

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private StatusOfReports status = StatusOfReports.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
