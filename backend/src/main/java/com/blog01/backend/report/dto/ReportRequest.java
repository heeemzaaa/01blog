package com.blog01.backend.report.dto;

import java.util.UUID;

import com.blog01.backend.report.model.Report.Target;

import lombok.Data;

@Data
public class ReportRequest {
    private Target targetType;
    private UUID targetId;
    private String reason;
    private String description;
}
