package com.blog01.backend.report.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.model.Notification.NotificationType;
import com.blog01.backend.notification.service.NotificationService;
import com.blog01.backend.post.repository.CommentRepository;
import com.blog01.backend.post.repository.PostRepository;
import com.blog01.backend.report.dto.ReportRequest;
import com.blog01.backend.report.model.Report;
import com.blog01.backend.report.model.Report.StatusOfReports;
import com.blog01.backend.report.repository.ReportRepository;
import com.blog01.backend.report.response.ReportResponse;
import java.util.UUID;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    public ResponseData<ReportResponse> createReport(String email, ReportRequest request) {
        User reporter = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not Found !"));

        boolean targetExists;

        if (request.getTargetType() == Report.Target.POST) {
            targetExists = postRepository.existsById(request.getTargetId());
        } else if (request.getTargetType() == Report.Target.COMMENT) {
            targetExists = commentRepository.existsById(request.getTargetId());
        } else if (request.getTargetType() == Report.Target.USER) {
            targetExists = userRepository.existsById(request.getTargetId());
        } else {
            targetExists = false;
        }

        if (!targetExists) {
            return ResponseData.error("The " + request.getTargetType() + " you are trying to report does not exist.");
        }

        Report report = Report.builder()
                .reporter(reporter)
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .description(request.getDescription())
                .status(StatusOfReports.PENDING)
                .build();

        Report saved = reportRepository.save(report);

        List<User> admins = userRepository.findByRole(User.Role.ADMIN);
        for (User admin : admins) {
            notificationService.sendNotification(
                    admin,
                    reporter,
                    NotificationType.REPORT_CREATED,
                    saved.getId());
        }
        return ResponseData.success("report done successfully !", mapToResponse(saved));
    }

    public ResponseData<List<ReportResponse>> getPendingReports() {
        List<Report> reports = reportRepository.findByStatusOrderByCreatedAtDesc(StatusOfReports.PENDING);
        List<ReportResponse> response = reports.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseData.success("Pending reports fetched", response);
    }

    public ResponseData<ReportResponse> updateReportStatus(UUID reportId, StatusOfReports newStatus) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(newStatus);
        Report saved = reportRepository.save(report);

        notificationService.sendNotification(
                report.getReporter(),
                null,
                NotificationType.REPORT_UPDATE,
                saved.getId());

        return ResponseData.success("Report status updated", mapToResponse(saved));
    }

    private ReportResponse mapToResponse(Report r) {
        return ReportResponse.builder()
                .id(r.getId())
                .targetType(r.getTargetType())
                .targetId(r.getTargetId())
                .reason(r.getReason())
                .description(r.getDescription())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .reporter(UserResponse.builder()
                        .id(r.getReporter().getId())
                        .username(r.getReporter().getUsername())
                        .build())
                .build();
    }
}