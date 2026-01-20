package com.blog01.backend.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog01.backend.report.model.Report;
import com.blog01.backend.report.model.Report.StatusOfReports;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByStatusOrderByCreatedAtDesc(StatusOfReports status);
}
