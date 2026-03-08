package com.blog01.backend.report.dto;

import java.util.UUID;

import com.blog01.backend.report.model.Report.Target;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportRequest {

    @NotNull(message = "Target type is required")
    private Target targetType;

    @NotNull(message = "Target id is required")
    private UUID targetId;

    @NotBlank(message = "Reason is required")
    @Size(min = 3, message = "Reason must contain at least 3 characters")
    private String reason;

    @NotBlank(message = "Description is required")
    @Size(min = 3, message = "Description must contain at least 3 characters")
    private String description;
}
