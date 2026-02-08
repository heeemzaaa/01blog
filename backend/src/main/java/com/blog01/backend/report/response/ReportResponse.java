package com.blog01.backend.report.response;

import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.report.model.Report;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReportResponse {

    private UUID id;
    private UserResponse reporter;
    private Report.Target targetType;
    private UUID targetId;
    private String reason;
    private String description;
    private Report.StatusOfReports status;
    private LocalDateTime createdAt;

    public static ReportResponse fromEntity(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .reporter(
                        UserResponse.builder()
                                .id(report.getReporter().getId())
                                .username(report.getReporter().getUsername())
                                .firstName(report.getReporter().getFirstName())
                                .lastName(report.getReporter().getLastName())
                                .profileImage(report.getReporter().getProfileImage())
                                .build())
                .targetType(report.getTargetType())
                .targetId(report.getTargetId())
                .reason(report.getReason())
                .description(report.getDescription())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
