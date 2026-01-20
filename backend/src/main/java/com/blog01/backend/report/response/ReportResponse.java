package com.blog01.backend.report.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.report.model.Report.StatusOfReports;
import com.blog01.backend.report.model.Report.Target;

@Data
@Builder
public class ReportResponse {
    private UUID id;
    private UserResponse reporter;
    private Target targetType;
    private UUID targetId;
    private String reason;
    private String description;
    private StatusOfReports status;
    private LocalDateTime createdAt;
}
