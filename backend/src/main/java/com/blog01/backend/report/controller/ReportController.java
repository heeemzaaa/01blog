package com.blog01.backend.report.controller;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.report.dto.ReportRequest;
import com.blog01.backend.report.response.ReportResponse;
import com.blog01.backend.report.model.Report.StatusOfReports;
import com.blog01.backend.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    @PostMapping("/api/reports")
    public ResponseEntity<ResponseData<ReportResponse>> createReport(@RequestBody ReportRequest request, Principal principal) {
        return ResponseEntity.ok(reportService.createReport(principal.getName(), request));
    }

    // Only for admins
    @GetMapping("/api/admin/reports")
    public ResponseEntity<ResponseData<List<ReportResponse>>> getPendingReports() {
        return ResponseEntity.ok(reportService.getPendingReports());
    }

    @PutMapping("/api/admin/reports/{id}")
    public ResponseEntity<ResponseData<ReportResponse>> updateReportStatus(@PathVariable UUID id, @RequestParam StatusOfReports status) {
        return ResponseEntity.ok(reportService.updateReportStatus(id, status));
    }
}