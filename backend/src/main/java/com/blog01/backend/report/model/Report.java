package com.blog01.backend.report.model;

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
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
    private String targetId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String description;

    public enum StatusOfReports {
        PENDING,
        REVIEWED,
        DISMISSED,
        ACTIONED
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOfReports status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
